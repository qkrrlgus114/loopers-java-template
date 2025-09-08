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
        PENDING, // 발행 대기
        PUBLISHED, // 발행 완료
        FAILED // 발행 실패
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


    public void markAsPublished() {
        if (this.status != OutboxStatus.PUBLISHED) {
            throw new IllegalStateException("이미 발행된 이벤트입니다.");
        }
        this.status = OutboxStatus.PUBLISHED;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        if (this.status == OutboxStatus.FAILED) {
            throw new IllegalStateException("이미 실패한 이벤트입니다.");
        }
        this.status = OutboxStatus.FAILED;
        this.retryCount += 1;
    }

    public boolean canRetry() {
        return this.status == OutboxStatus.FAILED && this.retryCount < 3;
    }

}
