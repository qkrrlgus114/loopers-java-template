package com.loopers.domain.event;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OutboxRepository {
    Outbox save(Outbox outbox);

    List<Outbox> findByStatusOrderByCreatedAt(Outbox.OutboxStatus status, Pageable pageable);

    List<Outbox> findFailedEventsForRetry(int maxRetryCount, Pageable pageable);
}
