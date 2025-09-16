import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// 사용자 정의 메트릭
export let errorRate = new Rate('errors');

// 테스트 옵션 설정
export let options = {
  // 10만번 좋아요 실행
  iterations: 100000,
  
  // VUs (Virtual Users) 설정 - 동시 실행 사용자 수
  vus: 100,
  
  // 또는 단계별 부하 증가를 원한다면 아래 주석 해제
  // stages: [
  //   { duration: '2m', target: 50 },   // 2분 동안 50 VUs까지 증가
  //   { duration: '5m', target: 100 },  // 5분 동안 100 VUs 유지
  //   { duration: '2m', target: 0 },    // 2분 동안 0 VUs로 감소
  // ],
  
  thresholds: {
    // 95%의 요청이 3초 안에 완료되어야 함 (좋아요는 상세조회보다 무거울 수 있음)
    http_req_duration: ['p(95)<3000'],
    // 에러율이 10% 미만이어야 함 (좋아요는 중복/취소 상황이 많을 수 있음)
    errors: ['rate<0.10'],
    // 초당 처리량 최소 30 RPS
    http_reqs: ['rate>30'],
  },
};

// 테스트 설정
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const LIKE_API_ENDPOINT = '/api/v1/product-likes';

// 랜덤 범위 설정
const MIN_USER_ID = 1;
const MAX_USER_ID = 1000;
const MIN_PRODUCT_ID = 1;
const MAX_PRODUCT_ID = 100000;

// 좋아요/취소 비율 (70% 좋아요, 30% 취소)
const LIKE_PROBABILITY = 0.7;

/**
 * 지정된 범위에서 랜덤한 정수를 생성
 */
function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

/**
 * 메인 테스트 함수
 */
export default function() {
  // 랜덤 사용자 ID (1~1000)
  const randomUserId = getRandomInt(MIN_USER_ID, MAX_USER_ID);
  
  // 랜덤 상품 ID (1~100000)
  const randomProductId = getRandomInt(MIN_PRODUCT_ID, MAX_PRODUCT_ID);
  
  // 좋아요 또는 취소 결정
  const isLike = Math.random() < LIKE_PROBABILITY;
  
  const requestBody = {
    productId: randomProductId,
    memberId: randomUserId
  };
  
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    timeout: '15s', // 타임아웃 15초 (좋아요는 상세조회보다 시간이 오래 걸릴 수 있음)
  };
  
  let response;
  let operationType;
  
  if (isLike) {
    // 좋아요 등록
    operationType = 'LIKE';
    response = http.post(`${BASE_URL}${LIKE_API_ENDPOINT}`, JSON.stringify(requestBody), params);
  } else {
    // 좋아요 취소
    operationType = 'UNLIKE';
    response = http.del(`${BASE_URL}${LIKE_API_ENDPOINT}`, JSON.stringify(requestBody), params);
  }
  
  // 응답 검증
  const isSuccess = check(response, {
    'status code is 200 or 400': (r) => r.status === 200 || r.status === 400, // 400은 중복/없음 상황에서 정상
    'response time < 3000ms': (r) => r.timings.duration < 3000,
    'response has success field': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.hasOwnProperty('success');
      } catch (e) {
        return false;
      }
    },
    'response has data field': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.hasOwnProperty('data');
      } catch (e) {
        return false;
      }
    }
  });
  
  // 에러율 메트릭 업데이트 (500번대 에러만 실제 에러로 간주)
  const isActualError = response.status >= 500;
  errorRate.add(isActualError);
  
  // 로그 출력 (1% 확률로)
  if (Math.random() < 0.01) {
    let resultMessage = '';
    if (response.status === 200) {
      resultMessage = '성공';
    } else if (response.status === 400) {
      resultMessage = '중복/존재하지않음';
    } else {
      resultMessage = '에러';
    }
    
    console.log(`User ${randomUserId} ${operationType} Product ${randomProductId} - Status: ${response.status} (${resultMessage}), Duration: ${response.timings.duration}ms`);
  }
  
  // 실제 사용자 행동을 시뮬레이션하기 위한 대기 시간
  sleep(Math.random() * 1.0); // 0~1초 랜덤 대기
}

/**
 * 테스트 시작 시 실행되는 함수
 */
export function setup() {
  console.log('=== 상품 좋아요 로드 테스트 시작 ===');
  console.log(`Base URL: ${BASE_URL}`);
  console.log(`사용자 ID 범위: ${MIN_USER_ID} ~ ${MAX_USER_ID}`);
  console.log(`상품 ID 범위: ${MIN_PRODUCT_ID} ~ ${MAX_PRODUCT_ID}`);
  console.log(`총 실행 횟수: ${options.iterations}회`);
  console.log(`동시 사용자 수: ${options.vus}명`);
  console.log(`좋아요 비율: ${LIKE_PROBABILITY * 100}%, 취소 비율: ${(1 - LIKE_PROBABILITY) * 100}%`);
  console.log('================================================');
  
  // 서버 상태 확인
  const healthCheck = http.get(`${BASE_URL}/actuator/health`, {
    timeout: '5s'
  });
  
  if (healthCheck.status !== 200) {
    console.warn(`경고: 서버 상태 확인 실패 (Status: ${healthCheck.status})`);
  } else {
    console.log('서버 상태 확인 완료 ✓');
  }
  
  // 샘플 API 테스트 (좋아요)
  const sampleLikeTest = http.post(`${BASE_URL}${LIKE_API_ENDPOINT}`, JSON.stringify({
    productId: 1,
    memberId: 1
  }), {
    headers: {
      'Content-Type': 'application/json',
    },
    timeout: '5s'
  });
  
  console.log(`샘플 좋아요 테스트 - Status: ${sampleLikeTest.status}`);
  
  // 샘플 API 테스트 (취소)
  const sampleUnlikeTest = http.del(`${BASE_URL}${LIKE_API_ENDPOINT}`, JSON.stringify({
    productId: 1,
    memberId: 1
  }), {
    headers: {
      'Content-Type': 'application/json',
    },
    timeout: '5s'
  });
  
  console.log(`샘플 좋아요 취소 테스트 - Status: ${sampleUnlikeTest.status}`);
}

/**
 * 테스트 종료 시 실행되는 함수
 */
export function teardown(data) {
  console.log('=== 상품 좋아요 로드 테스트 완료 ===');
  console.log('상세 결과는 k6 리포트를 확인하세요.');
  console.log('주의: 400 상태코드는 중복 좋아요/존재하지 않는 좋아요 취소로 정상적인 응답입니다.');
}

/**
 * 각 VU가 테스트를 시작하기 전에 실행
 */
export function setupVU() {
  // VU별 초기화 작업이 필요한 경우 여기에 작성
}

/**
 * 각 VU가 테스트를 끝낸 후 실행
 */
export function teardownVU() {
  // VU별 정리 작업이 필요한 경우 여기에 작성
}