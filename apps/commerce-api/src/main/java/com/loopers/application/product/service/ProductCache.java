package com.loopers.application.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.application.product.result.ProductListPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
class ProductCache {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper om;

    // 기본 TTL(밀리초) + ±20% 지터
    private long ttlWithJitter(long baseMillis) {
        double jitter = (0.8 + (Math.random() * 0.4));
        return (long) (baseMillis * jitter);
    }

    public void putList(String key, ProductListPage value, long baseTtlMillis) {
        redisTemplate
                .opsForValue()
                .set(key, value, ttlWithJitter(baseTtlMillis), TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    public ProductListPage getList(String key) {
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return null;
        // Map/LinkedHashMap -> ProductListPage 로 변환
        return om.convertValue(raw, ProductListPage.class);
    }
}
