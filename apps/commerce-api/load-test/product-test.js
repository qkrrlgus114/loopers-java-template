import http from 'k6/http';

export const options = {
    stages: [
        {duration: '1m', target: 100}, // 1분 동안 100명까지 증가
        {duration: '30s', target: 0},  // 정리
    ],
};

const BASE = 'http://localhost:8080';

export default function () {
    const sort = null;
    const page = Math.floor(Math.random() * 5) + 1; // 1~5 랜덤 페이지
    // const brands = '1';
    // const minPrice = 0;
    // const maxPrice = 1000000;

    const url = `${BASE}/api/v1/products?page=${page}`;

    http.get(url);
}