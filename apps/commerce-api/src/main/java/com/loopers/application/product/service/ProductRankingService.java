package com.loopers.application.product.service;

import com.loopers.redis.repository.RankingInMemoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 랭킹 조회 서비스 (commerce-api)
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
        CATEGORY("category"), // 카테고리별 랭킹 (향후 확장)
        BRAND("brand"),       // 브랜드별 랭킹 (향후 확장)
        REGION("region");     // 지역별 랭킹 (향후 확장)

        private final String value;

        RankingScope(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

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
     * 오늘의 상위 랭킹 상품 ID 조회 (페이징 지원)
     */
    public List<Long> getTodayTopProductIds(int page, int limit) {
        String key = getTodayRankingKey();

        // 페이징 계산: start = (page - 1) * limit, end = page * limit - 1
        long start = (long) (page - 1) * limit;
        long end = (long) page * limit - 1;

        List<String> topProducts = rankingInMemoryRepo.getRangeRanking(key, start, end);

        List<Long> productIds = new ArrayList<>();
        for (String productId : topProducts) {
            try {
                productIds.add(Long.parseLong(productId));
            } catch (NumberFormatException e) {
                log.warn("잘못된 상품 ID 형식: {}", productId);
            }
        }

        log.info("오늘의 상위 랭킹 상품 ID 조회 - key: {}, page: {}, limit: {}, start: {}, end: {}, result: {}",
                key, page, limit, start, end, productIds);
        return productIds;
    }

    /**
     * 특정 상품의 오늘 랭킹 점수 조회
     */
    public Double getTodayScore(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);

        Double score = rankingInMemoryRepo.getScore(key, member);
        return score != null ? score : 0.0;
    }

    /**
     * 특정 상품의 오늘 순위 조회 (1부터 시작)
     */
    public Long getTodayRank(Long productId) {
        String key = getTodayRankingKey();
        String member = String.valueOf(productId);

        Long rank = rankingInMemoryRepo.getRank(key, member);

        if (rank != null) {
            return rank + 1; // 1부터 시작하도록 조정
        }

        return null; // 랭킹에 없음
    }

    /**
     * 특정 날짜의 랭킹 상품 ID 조회 (페이징 지원)
     */
    public List<Long> getRankingProductIdsByDate(LocalDate date, int page, int size) {
        String key = getRankingKey(RankingScope.ALL, date);

        // 페이징 계산: start = (page - 1) * size, end = page * size - 1
        long start = (long) (page - 1) * size;
        long end = (long) page * size - 1;

        List<String> rankingProducts = rankingInMemoryRepo.getRangeRanking(key, start, end);

        List<Long> productIds = new ArrayList<>();
        for (String productId : rankingProducts) {
            try {
                productIds.add(Long.parseLong(productId));
            } catch (NumberFormatException e) {
                log.warn("잘못된 상품 ID 형식: {}", productId);
            }
        }

        log.info("특정 날짜 랭킹 상품 ID 조회 - key: {}, date: {}, page: {}, size: {}, start: {}, end: {}, result: {}",
                key, date, page, size, start, end, productIds);
        return productIds;
    }

    /**
     * 특정 상품의 특정 날짜 랭킹 점수 조회
     */
    public Double getScoreByDate(Long productId, LocalDate date) {
        String key = getRankingKey(RankingScope.ALL, date);
        String member = String.valueOf(productId);

        Double score = rankingInMemoryRepo.getScore(key, member);
        return score != null ? score : 0.0;
    }
}
