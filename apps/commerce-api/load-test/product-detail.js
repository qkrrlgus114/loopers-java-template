import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// 사용자 정의 메트릭
export let errorRate = new Rate('errors');

// 테스트 옵션 설정
export let options = {
  // 10만번 실행을 위한 설정
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
    // 95%의 요청이 2초 안에 완료되어야 함
    http_req_duration: ['p(95)<2000'],
    // 에러율이 5% 미만이어야 함
    errors: ['rate<0.05'],
    // 초당 처리량 최소 50 RPS
    http_reqs: ['rate>50'],
  },
};

// 테스트 설정
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const API_ENDPOINT = '/api/v1/products';

// 랜덤 범위 설정
const MIN_USER_ID = 1;
const MAX_USER_ID = 1000;
const MIN_PRODUCT_ID = 1;
const MAX_PRODUCT_ID = 100000;

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
  
  // 상품 상세조회 API 호출
  const url = `${BASE_URL}${API_ENDPOINT}/${randomProductId}?memberId=${randomUserId}`;
  
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    timeout: '10s', // 타임아웃 10초
  };
  
  // HTTP 요청 실행
  const response = http.get(url, params);
  
  // 응답 검증
  const isSuccess = check(response, {
    'status code is 200': (r) => r.status === 200,
    'response time < 2000ms': (r) => r.timings.duration < 2000,
    'response has success field': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.hasOwnProperty('success');
      } catch (e) {
        return false;
      }
    },
    'response data is not null': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.data !== null && body.data !== undefined;
      } catch (e) {
        return false;
      }
    }
  });
  
  // 에러율 메트릭 업데이트
  errorRate.add(!isSuccess);
  
  // 로그 출력 (일부 요청에 대해서만)
  if (Math.random() < 0.01) { // 1% 확률로 로그 출력
    console.log(`User ${randomUserId} requested product ${randomProductId} - Status: ${response.status}, Duration: ${response.timings.duration}ms`);
  }
  
  // 실제 사용자 행동을 시뮬레이션하기 위한 약간의 대기 시간
  sleep(Math.random() * 0.5); // 0~0.5초 랜덤 대기
}

/**
 * 테스트 시작 시 실행되는 함수
 */
export function setup() {
  console.log('=== 상품 상세조회 로드 테스트 시작 ===');
  console.log(`Base URL: ${BASE_URL}`);
  console.log(`사용자 ID 범위: ${MIN_USER_ID} ~ ${MAX_USER_ID}`);
  console.log(`상품 ID 범위: ${MIN_PRODUCT_ID} ~ ${MAX_PRODUCT_ID}`);
  console.log(`총 실행 횟수: ${options.iterations}회`);
  console.log(`동시 사용자 수: ${options.vus}명`);
  console.log('==========================================');
  
  // 서버 상태 확인
  const healthCheck = http.get(`${BASE_URL}/actuator/health`, {
    timeout: '5s'
  });
  
  if (healthCheck.status !== 200) {
    console.warn(`경고: 서버 상태 확인 실패 (Status: ${healthCheck.status})`);
  } else {
    console.log('서버 상태 확인 완료 ✓');
  }
}

/**
 * 테스트 종료 시 실행되는 함수
 */
export function teardown(data) {
  console.log('=== 상품 상세조회 로드 테스트 완료 ===');
  console.log('상세 결과는 k6 리포트를 확인하세요.');
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