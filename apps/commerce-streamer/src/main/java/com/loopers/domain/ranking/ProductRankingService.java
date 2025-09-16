package com.loopers.domain.ranking;

import com.loopers.redis.repository.RankingInMemoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Redis ZSET을 이용한 상품 랭킹 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final RankingInMemoryRepo rankingInMemoryRepo;

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
     * 상품 조회 점수 추가 (가중치: 0.1)
     */
    public void addViewScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = VIEW_WEIGHT * 1.0;

        rankingInMemoryRepo.addScore(key, member, score);

        // TTL 설정 (3일)
        rankingInMemoryRepo.setTtl(key, Duration.ofDays(3));

        log.debug("상품 조회 점수 추가 - productId: {}, score: {}, key: {}", productId, score, key);
    }

    /**
     * 상품 좋아요 점수 추가 (가중치: 0.2)
     */
    public void addLikeScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = LIKE_WEIGHT * 1.0;

        rankingInMemoryRepo.addScore(key, member, score);

        // TTL 설정 (3일)
        rankingInMemoryRepo.setTtl(key, Duration.ofDays(3));

        log.debug("상품 좋아요 점수 추가 - productId: {}, score: {}, key: {}", productId, score, key);
    }

    /**
     * 상품 좋아요 취소 점수 차감 (가중치: -0.2)
     */
    public void removeLikeScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);
        double score = -LIKE_WEIGHT * 1.0;

        rankingInMemoryRepo.addScore(key, member, score);

        // TTL 설정 (3일)
        rankingInMemoryRepo.setTtl(key, Duration.ofDays(3));

        log.debug("상품 좋아요 취소 점수 차감 - productId: {}, score: {}, key: {}", productId, score, key);
    }

}
