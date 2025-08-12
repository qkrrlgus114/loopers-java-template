import http from 'k6/http';

export const options = {
    stages: [
        {duration: '30s', target: 1},    // 1명으로 시작
        {duration: '2m', target: 100},  // 2분 동안 100명까지 증가
        {duration: '1m', target: 100},  // 1분 유지
        {duration: '30s', target: 0},    // 정리
    ],
};

const BASE = 'http://localhost:8080';

export default function () {
    const sort = 'LIKE_COUNT_DESC';
    const page = 999000;
    const brands = '1';
    const minPrice = 0;
    const maxPrice = 1000000;

    const url = `${BASE}/api/v1/products?sort=${sort}&page=${page}&brands=${brands}&minPrice=${minPrice}&maxPrice=${maxPrice}`;

    http.get(url);

}