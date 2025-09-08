package com.loopers.infrastructure.event.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.application.event.Event;
import com.loopers.application.event.EventPublisher;
import com.loopers.domain.event.Outbox;
import com.loopers.domain.event.OutboxRepository;
import com.loopers.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventPublisher implements EventPublisher {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public void publish(Event event) {
        try {
// 1. 토픽 결정
            String topic = resolveTopicFor(event.getEventType());

            // 2. 이벤트를 JSON으로 변환
            String payload = objectMapper.writeValueAsString(event);

            // 3. Outbox 테이블에 저장
            Outbox outbox = Outbox.create(
                    event.getAggregateId(),
                    event.getEventType(),
                    topic,
                    payload,
                    event.getVersion()
            );

            outboxRepository.save(outbox);

            log.debug("이벤트를 Outbox에 저장 - type: {}, aggregateId: {}",
                    event.getEventType(), event.getAggregateId());

        } catch (Exception e) {
            log.error("outbox 이벤트 발행 실패: {}", e.getMessage(), e);
            throw new RuntimeException("outbox 이벤트 발행 실패", e);
        }
    }

    private String resolveTopicFor(String eventType) {
        // 이벤트 타입에 따라 토픽 결정

        // catalog-events 토픽
        if (eventType.contains("Like") ||
                eventType.equals("Added") ||
                eventType.equals("Removed") ||
                eventType.contains("Stock") ||
                eventType.equals("Changed")) {
            return KafkaTopics.CATALOG_EVENTS;
        }

        // order-events 토픽
        if (eventType.contains("Order") ||
                eventType.equals("Created") ||
                eventType.equals("Confirmed") ||
                eventType.equals("Cancelled") ||
                eventType.contains("Payment") ||
                eventType.equals("Completed") ||
                eventType.equals("Failed")) {
            return KafkaTopics.ORDER_EVENTS;
        }

        log.warn("알 수 없는 이벤트 타입: {}, 기본 토픽 사용", eventType);
        return KafkaTopics.CATALOG_EVENTS;
    }

}
