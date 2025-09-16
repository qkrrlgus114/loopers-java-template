package com.loopers.redis.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Redis 기반 CacheTemplate 구현체
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCacheTemplate implements CacheTemplate {

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Optional<T> getOrSet(String key, Class<T> clazz, CacheLoader<T> loader, Duration ttl) {
        try {
            // 캐시에서 조회 시도
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached != null) {
                T value = objectMapper.convertValue(cached, clazz);
                return Optional.of(value);
            }

            // 캐시 미스 시 로더를 통해 데이터 로드
            T loadedValue = loader.load();
            if (loadedValue != null) {
                if (ttl != null) {
                    redisTemplate.opsForValue().set(key, loadedValue, ttl);
                } else {
                    redisTemplate.opsForValue().set(key, loadedValue);
                }
                return Optional.of(loadedValue);
            }

            return Optional.empty();

        } catch (Exception e) {
            log.error("조회 또는 설정 실패 - key: {}", key, e);
            try {
                // 캐시 실패 시 직접 로드
                T fallbackValue = loader.load();
                return Optional.ofNullable(fallbackValue);
            } catch (Exception fallbackError) {
                log.error("조회 실패 및 로드 실패 - key: {}", key, fallbackError);
                return Optional.empty();
            }
        }
    }

    @Override
    public <T> List<T> range(String key, int count, Class<T> clazz) {
        return range(key, 0, count - 1, clazz);
    }

    @Override
    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        try {
            Set<String> rangeResult = stringRedisTemplate.opsForZSet()
                    .reverseRange(key, start, end);

            if (rangeResult == null || rangeResult.isEmpty()) {
                return new ArrayList<>();
            }

            return rangeResult.stream()
                    .map(item -> {
                        try {
                            return objectMapper.convertValue(item, clazz);
                        } catch (Exception e) {
                            log.warn("변환 실패 - item: {}, target class: {}", item, clazz.getName(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("범위 랭킹 조회 실패 - key: {}, start: {}, end: {}", key, start, end, e);
            return new ArrayList<>();
        }
    }

    @Override
    public void zadd(String key, String member, double score) {
        try {
            stringRedisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            log.error("추가 작업 실패 - key: {}, member: {}, score: {}", key, member, score, e);
            throw new RuntimeException("추가 작업 실패", e);
        }
    }

    @Override
    public void zincrby(String key, String member, double increment) {
        try {
            stringRedisTemplate.opsForZSet().incrementScore(key, member, increment);
        } catch (Exception e) {
            log.error("ZSet increment failed for key: {}, member: {}, increment: {}", key, member, increment, e);
            throw new RuntimeException("증가 작업 실패", e);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        try {
            return stringRedisTemplate.opsForZSet().score(key, member);
        } catch (Exception e) {
            log.error("랭킹 점수 조회 실패 - key: {}, member: {}", key, member, e);
            return null;
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        try {
            return stringRedisTemplate.opsForZSet().reverseRank(key, member);
        } catch (Exception e) {
            log.error("랭킹 조회 실패 - key: {}, member: {}", key, member, e);
            return null;
        }
    }

    @Override
    public void expire(String key, Duration duration) {
        try {
            redisTemplate.expire(key, duration);
        } catch (Exception e) {
            log.error("만료 설정 실패 - key: {}, duration: {}", key, duration, e);
            throw new RuntimeException("캐시 만료 설정 실패", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("존재 여부 확인 실패 - key: {}", key, e);
            throw new RuntimeException("캐시 삭제 실패", e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("존재 여부 확인 실패 - key: {}", key, e);
            return false;
        }
    }
}
