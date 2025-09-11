package com.loopers.redis.template;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Redis 캐시 추상화 템플릿
 */
public interface CacheTemplate {

    /**
     * 키에 해당하는 값을 조회하거나, 없으면 supplier를 통해 값을 생성하여 TTL과 함께 저장
     */
    <T> Optional<T> getOrSet(String key, Class<T> clazz, CacheLoader<T> loader, Duration ttl);


    /**
     * ZSet에서 상위 count개의 요소를 점수 높은 순으로 조회
     */
    <T> List<T> range(String key, int count, Class<T> clazz);

    /**
     * ZSet에서 start ~ end 범위의 요소를 점수 높은 순으로 조회
     */
    <T> List<T> range(String key, long start, long end, Class<T> clazz);

    /**
     * ZSet에 멤버와 점수 추가
     */
    void zadd(String key, String member, double score);

    /**
     * ZSet에 멤버의 점수 증가
     */
    void zincrby(String key, String member, double increment);

    /**
     * ZSet에서 멤버의 점수 조회
     */
    Double zscore(String key, String member);

    /**
     * ZSet에서 멤버의 순위 조회 (점수 높은 순)
     */
    Long zrevrank(String key, String member);

    /**
     * 키에 TTL 설정
     */
    void expire(String key, Duration duration);

    /**
     * 키 삭제
     */
    void delete(String key);

    /**
     * 키 존재 여부 확인
     */
    boolean exists(String key);

    @FunctionalInterface
    interface CacheLoader<T> {
        T load() throws Exception;
    }
}
