package com.loopers.domain.ranking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Redis ZSET을 이용한 상품 랭킹 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final StringRedisTemplate stringRedisTemplate;

    // 개선된 키 전략
    private static final String KEY_PREFIX = "loopers:rank:v1:product:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 스코프 정의
    public enum RankingScope {
        ALL("all"),           // 전체 상품 랭킹
        CATEGORY("category"), // 카테고리별 랭킹
        BRAND("brand");       // 브랜드별 랭킹

        private final String value;

        RankingScope(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // 스코어 가중치
    public static final double VIEW_WEIGHT = 0.1;
    public static final double LIKE_WEIGHT = 0.2;

    /**
     * 오늘자 랭킹 키 생성
     */
    private String getTodayRankingKey() {
        return getRankingKey(RankingScope.ALL, LocalDate.now());
    }

    /**
     * 특정 스코프와 날짜의 랭킹 키 생성
     * 형태: loopers:rank:v1:product:{scope}:{YYYYMMDD}
     */
    private String getRankingKey(RankingScope scope, LocalDate date) {
        return KEY_PREFIX + scope.getValue() + ":" + date.format(DATE_FORMATTER);
    }

    /**
     * 레거시 키 형태 생성 (마이그레이션 용)
     */
    private String getLegacyKey(LocalDate date) {
        return "ranking:all:" + date.format(DATE_FORMATTER);
    }

    /**
     * 상품 조회 점수 추가 (가중치: 0.1)
     */
    public void addViewScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = VIEW_WEIGHT * 1.0;

        stringRedisTemplate.opsForZSet().incrementScore(key, member, score);

        // TTL 설정 (3일)
        stringRedisTemplate.expire(key, java.time.Duration.ofDays(3));

        log.debug("상품 조회 점수 추가 - productId: {}, score: {}, key: {}", productId, score, key);
    }

    /**
     * 상품 좋아요 점수 추가 (가중치: 0.2)
     */
    public void addLikeScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = LIKE_WEIGHT * 1.0;

        stringRedisTemplate.opsForZSet().incrementScore(key, member, score);
        stringRedisTemplate.expire(key, java.time.Duration.ofDays(3));

        log.debug("상품 좋아요 점수 추가 - productId: {}, score: {}, key: {}", productId, score, key);
    }

    /**
     * 상품 좋아요 취소 점수 차감 (가중치: -0.2)
     */
    public void removeLikeScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = -LIKE_WEIGHT * 1.0;

        stringRedisTemplate.opsForZSet().incrementScore(key, member, score);

        log.debug("상품 좋아요 취소 점수 차감 - productId: {}, score: {}, key: {}", productId, score, key);
    }

    /**
     * 오늘의 상위 랭킹 상품 조회
     */
    public Set<String> getTodayTopProducts(int limit) {
        String key = getTodayRankingKey();
        // ZREVRANGE: 점수가 높은 순서대로 조회
        Set<String> topProducts = stringRedisTemplate.opsForZSet()
                .reverseRange(key, 0, limit - 1);

        log.info("오늘의 상위 랭킹 조회 - key: {}, limit: {}, result: {}", key, limit, topProducts);
        return topProducts;
    }

    /**
     * 특정 상품의 오늘 랭킹 점수 조회
     */
    public Double getTodayScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);

        Double score = stringRedisTemplate.opsForZSet().score(key, member);
        log.debug("상품 점수 조회 - productId: {}, score: {}, key: {}", productId, score, key);

        return score != null ? score : 0.0;
    }

    /**
     * 특정 상품의 오늘 순위 조회 (1부터 시작)
     */
    public Long getTodayRank(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);

        // ZREVRANK: 점수가 높은 순서에서의 순위 (0부터 시작)
        Long rank = stringRedisTemplate.opsForZSet().reverseRank(key, member);

        if (rank != null) {
            rank = rank + 1; // 1부터 시작하도록 조정
        }

        log.debug("상품 순위 조회 - productId: {}, rank: {}, key: {}", productId, rank, key);
        return rank;
    }

    /**
     * 특정 날짜의 상위 랭킹 상품 조회
     */
    public Set<String> getTopProducts(LocalDate date, int limit) {
        String key = getRankingKey(RankingScope.ALL, date);
        Set<String> topProducts = stringRedisTemplate.opsForZSet()
                .reverseRange(key, 0, limit - 1);

        log.info("특정 날짜 상위 랭킹 조회 - date: {}, key: {}, limit: {}, result: {}",
                date, key, limit, topProducts);
        return topProducts;
    }

}
