package com.loopers.application.event.productlike;

import com.loopers.application.event.Event;

import java.time.LocalDateTime;

public record ProductLikedEvent(
        Long memberId,
        Long productId,
        LocalDateTime addedAt
) implements Event {

    public static ProductLikedEvent from(Long memberId, Long productId) {
        return new ProductLikedEvent(memberId, productId, LocalDateTime.now());
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return addedAt;
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(productId);
    }
}
