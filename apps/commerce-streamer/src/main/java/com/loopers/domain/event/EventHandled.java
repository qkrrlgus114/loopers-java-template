package com.loopers.domain.event;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 이벤트 처리 완료 엔티티
 * 멱등성 보장: 같은 이벤트가 여러번 와도 한번만 처리
 */
@Entity
@Table(
        name = "event_handled",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_handled",
                        columnNames = {"event_id", "consumer_name"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EventHandled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String consumerName;  // AUDIT_LOG, METRICS, CACHE_EVICT

    @Column(nullable = false)
    private String eventType;

    private Long eventVersion;  // 이벤트 버전 (순서 체크용)

    @Column(nullable = false)
    private LocalDateTime processedAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static EventHandled create(
            String eventId,
            String consumerName,
            String eventType,
            String aggregateId,
            Long eventVersion
    ) {
        return EventHandled.builder()
                .eventId(eventId)
                .consumerName(consumerName)
                .eventType(eventType)
                .aggregateId(aggregateId)
                .eventVersion(eventVersion)
                .processedAt(LocalDateTime.now())
                .build();
    }
}
