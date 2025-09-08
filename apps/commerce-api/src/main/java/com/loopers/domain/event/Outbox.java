package com.loopers.domain.event;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Outbox extends BaseEntity {

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String topic;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OutboxStatus status = OutboxStatus.PENDING;

    private LocalDateTime processedAt;

    @Builder.Default
    private Integer retryCount = 0;

    private Long eventVersion;

    @Version
    private Long version;

    public enum OutboxStatus {
        PENDING, // 처리 대기
        PROCESSED, // 처리 완료
        FAILED // 처리 실패
    }

    public static Outbox create(String aggregateId, String eventType,
                                String topic, String payload, Long eventVersion) {
        return Outbox.builder()
                .aggregateId(aggregateId)
                .eventType(eventType)
                .topic(topic)
                .payload(payload)
                .eventVersion(eventVersion)
                .build();
    }

}
