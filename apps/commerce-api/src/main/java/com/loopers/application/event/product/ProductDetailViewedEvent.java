package com.loopers.application.event.product;

import com.loopers.application.event.Event;

import java.time.LocalDateTime;

public record ProductDetailViewedEvent(
        Long productId,
        Long memberId,
        LocalDateTime viewedAt
) implements Event {

    public static ProductDetailViewedEvent from(Long productId, Long memberId) {
        return new ProductDetailViewedEvent(productId, memberId, LocalDateTime.now());
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return viewedAt;
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(productId);
    }
}