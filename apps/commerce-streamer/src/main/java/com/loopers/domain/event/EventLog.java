package com.loopers.domain.event;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 이벤트 감사 로그 엔티티
 * 모든 Kafka 이벤트를 저장하는 테이블
 */
@Entity
@Table(name = "event_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;  // 이벤트 고유 ID (UUID)

    @Column(nullable = false)
    private String eventType;  // OrderCreated, LikeAdded 등

    @Column(nullable = false)
    private String aggregateId;  // 도메인 ID (ex. productId, orderId etc)

    @Column(nullable = false)
    private String topicName;

    private Integer partitionNumber;

    private Long offsetValue;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private LocalDateTime eventTimestamp;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static EventLog create(
            String eventId,
            String eventType,
            String aggregateId,
            String topicName,
            Integer partitionNumber,
            Long offsetValue,
            String payload,
            LocalDateTime eventTimestamp
    ) {
        return EventLog.builder()
                .eventId(eventId)
                .eventType(eventType)
                .aggregateId(aggregateId)
                .topicName(topicName)
                .partitionNumber(partitionNumber)
                .offsetValue(offsetValue)
                .payload(payload)
                .eventTimestamp(eventTimestamp)
                .build();
    }
}

