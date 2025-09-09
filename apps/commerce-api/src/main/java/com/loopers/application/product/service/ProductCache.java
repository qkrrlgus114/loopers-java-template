package com.loopers.application.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.result.ProductListPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
@Slf4j
public class ProductCache {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper om;

    // 기본 TTL(밀리초) + ±20% 지터
    private long ttlWithJitter(long baseMillis) {
        double jitter = (0.8 + (Math.random() * 0.4));
        return (long) (baseMillis * jitter);
    }

    public void putList(String key, ProductListPage value, long baseTtlMillis) {
        try {
            String json = om.writeValueAsString(value);
            stringRedisTemplate.opsForValue()
                    .set(key, json, ttlWithJitter(baseTtlMillis), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Redis에 데이터 저장 실패: key={}", key, e);
        }
    }

    public ProductListPage getList(String key) {
        try {
            String json = stringRedisTemplate.opsForValue().get(key);
            return json != null ? om.readValue(json, ProductListPage.class) : null;
        } catch (Exception e) {
            log.error("Redis에서 데이터 조회 실패: key={}", key, e);
            return null;
        }
    }

    public void putDetail(String key, ProductDetailResult value, long baseTtlMillis) {
        try {
            String json = om.writeValueAsString(value);
            stringRedisTemplate.opsForValue()
                    .set(key, json, ttlWithJitter(baseTtlMillis), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Redis에 상품 상세 데이터 저장 실패: key={}", key, e);
        }
    }

    public ProductDetailResult getDetail(String key) {
        try {
            String json = stringRedisTemplate.opsForValue().get(key);
            return json != null ? om.readValue(json, ProductDetailResult.class) : null;
        } catch (Exception e) {
            log.error("Redis에서 상품 상세 데이터 조회 실패: key={}", key, e);
            return null;
        }
    }

    public void invalidateProductDetailCache(Long productId) {
        try {
            // productId에 해당하는 모든 캐시 키를 찾아서 삭제
            String pattern = String.format("product:detail:productId=%d:memberId=*", productId);
            Set<String> keys = stringRedisTemplate.keys(pattern);
            
            if (keys != null && !keys.isEmpty()) {
                stringRedisTemplate.delete(keys);
                log.info("상품 상세 캐시 무효화 완료: productId={}, 삭제된 캐시 수={}", productId, keys.size());
            }
        } catch (Exception e) {
            log.error("상품 상세 캐시 무효화 실패: productId={}", productId, e);
        }
    }
}
