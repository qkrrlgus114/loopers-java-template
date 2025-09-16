package com.loopers.application.event.productlike;

import com.loopers.application.event.Event;

import java.time.LocalDateTime;

public record ProductUnlikedEvent(
        Long memberId,
        Long productId,
        LocalDateTime removedAt
) implements Event {

    public static ProductUnlikedEvent from(Long memberId, Long productId) {
        return new ProductUnlikedEvent(memberId, productId, LocalDateTime.now());
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return removedAt;
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(productId);
    }
}
