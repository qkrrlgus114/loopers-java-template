package com.loopers.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 카탈로그(상품) 도메인 이벤트 페이로드
 */
public class CatalogEventPayload {

    private CatalogEventPayload() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 좋아요 추가 이벤트 페이로드
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LikeAdded {
        private Long productId;
        private String userId;
        private LocalDateTime addedAt;
    }

    /**
     * 좋아요 제거 이벤트 페이로드
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LikeRemoved {
        private Long productId;
        private String userId;
        private LocalDateTime removedAt;
    }

    /**
     * 재고 변경 이벤트 페이로드
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockChanged {
        private Long productId;
        private Integer previousQuantity;
        private Integer currentQuantity;
        private String changeReason;  // ORDERED, CANCELLED, ADJUSTED
        private LocalDateTime changedAt;
    }

    /**
     * 상품 조회 이력 이벤트 페이로드
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetailViewed {
        private Long productId;
        private String userId;
        private LocalDateTime changedAt;
    }
}
