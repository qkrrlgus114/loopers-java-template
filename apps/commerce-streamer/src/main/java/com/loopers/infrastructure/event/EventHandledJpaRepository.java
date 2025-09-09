package com.loopers.infrastructure.event;

import com.loopers.domain.event.EventHandled;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EventHandledJpaRepository extends JpaRepository<EventHandled, Long> {
    boolean existsByEventIdAndConsumerName(String eventId, String consumerName);

    @Query("SELECT e FROM EventHandled e " +
            "WHERE e.aggregateId = :aggregateId " +
            "AND e.consumerName = :consumerName " +
            "ORDER BY e.eventVersion DESC " +
            "LIMIT 1")
    Optional<EventHandled> findLatestVersion(
            @Param("aggregateId") String aggregateId,
            @Param("consumerName") String consumerName
    );
}
