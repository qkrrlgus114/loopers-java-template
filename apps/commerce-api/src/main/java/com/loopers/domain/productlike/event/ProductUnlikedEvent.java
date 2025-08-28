package com.loopers.domain.productlike.event;

public record ProductUnlikedEvent(Long productId, Long memberId) {
    public static ProductUnlikedEvent of(Long productId, Long memberId) {
        return new ProductUnlikedEvent(productId, memberId);
    }
}
