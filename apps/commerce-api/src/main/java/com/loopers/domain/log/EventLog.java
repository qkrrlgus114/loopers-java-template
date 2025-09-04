package com.loopers.domain.log;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_log")
public class EventLog extends BaseEntity {

    // 이벤트 구분
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private int eventVersion;

    // 이벤트 고유 식별자
    private String eventId;

    // 이벤트 발생 시각
    private LocalDateTime occurredAt;

    // 이벤트 원본 데이터
    @Column(columnDefinition = "TEXT")
    private String payload;

    private Integer payloadSize;

    // 소스 및 컨텍스트
    private String sourceService;
    private Long userId;
    private Long entityId;
    private String traceId;
    private String spanId;
    private String correlationId;

    // 처리 상태
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private String errorCode;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Integer retryCount;
    private LocalDateTime processedAt;
    private Long processingTimeMs;

    // 운영 환경
    private String hostname;
    private String instanceId;
}
