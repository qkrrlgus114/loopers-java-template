package com.loopers.redis.repository;

import com.loopers.redis.template.CacheTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

/**
 * 랭킹 데이터를 위한 인메모리 레포지토리 (Redis 기반)
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RankingInMemoryRepo {

    private final CacheTemplate cacheTemplate;

    /**
     * 랭킹 점수 추가/증가
     */
    public void addScore(String rankingKey, String memberId, double score) {
        try {
            cacheTemplate.zincrby(rankingKey, memberId, score);
            log.debug("랭킹 점수 추가 - key: {}, member: {}, score: {}", rankingKey, memberId, score);
        } catch (Exception e) {
            log.error("랭킹 점수 추가 실패 - key: {}, member: {}, score: {}", rankingKey, memberId, score, e);
            throw e;
        }
    }

    /**
     * 상위 랭킹 조회 (점수 높은 순)
     */
    public List<String> getTopRanking(String rankingKey, int count) {
        try {
            List<String> result = cacheTemplate.range(rankingKey, count, String.class);
            log.debug("상위 랭킹 조회 - key: {}, count: {}, result size: {}", rankingKey, count, result.size());
            return result;
        } catch (Exception e) {
            log.error("상위 랭킹 조회 실패 - key: {}, count: {}", rankingKey, count, e);
            throw e;
        }
    }

    /**
     * 범위별 랭킹 조회 (페이징 지원)
     */
    public List<String> getRangeRanking(String rankingKey, long start, long end) {
        try {
            List<String> result = cacheTemplate.range(rankingKey, start, end, String.class);
            log.debug("범위 랭킹 조회 - key: {}, start: {}, end: {}, result size: {}",
                    rankingKey, start, end, result.size());
            return result;
        } catch (Exception e) {
            log.error("범위 랭킹 조회 실패 - key: {}, start: {}, end: {}", rankingKey, start, end, e);
            throw e;
        }
    }

    /**
     * 특정 멤버의 점수 조회
     */
    public Double getScore(String rankingKey, String memberId) {
        try {
            Double score = cacheTemplate.zscore(rankingKey, memberId);
            log.debug("멤버 점수 조회 - key: {}, member: {}, score: {}", rankingKey, memberId, score);
            return score;
        } catch (Exception e) {
            log.error("멤버 점수 조회 실패 - key: {}, member: {}", rankingKey, memberId, e);
            return null;
        }
    }

    /**
     * 특정 멤버의 순위 조회 (점수 높은 순, 0부터 시작)
     */
    public Long getRank(String rankingKey, String memberId) {
        try {
            Long rank = cacheTemplate.zrevrank(rankingKey, memberId);
            log.debug("멤버 순위 조회 - key: {}, member: {}, rank: {}", rankingKey, memberId, rank);
            return rank;
        } catch (Exception e) {
            log.error("멤버 순위 조회 실패 - key: {}, member: {}", rankingKey, memberId, e);
            return null;
        }
    }

    /**
     * 랭킹 키에 TTL 설정
     */
    public void setTtl(String rankingKey, Duration duration) {
        try {
            cacheTemplate.expire(rankingKey, duration);
            log.debug("랭킹 TTL 설정 - key: {}, duration: {}", rankingKey, duration);
        } catch (Exception e) {
            log.error("랭킹 TTL 설정 실패 - key: {}, duration: {}", rankingKey, duration, e);
            throw e;
        }
    }
}
