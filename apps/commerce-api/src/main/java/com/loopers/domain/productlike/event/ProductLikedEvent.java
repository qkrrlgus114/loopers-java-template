package com.loopers.domain.productlike.event;

public record ProductLikedEvent(Long productId, Long memberId) {
    public static ProductLikedEvent of(Long productId, Long memberId) {
        return new ProductLikedEvent(productId, memberId);
    }
}
