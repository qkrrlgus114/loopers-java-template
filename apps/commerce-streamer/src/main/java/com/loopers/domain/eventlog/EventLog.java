package com.loopers.domain.eventlog;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "event_log")
public class EventLog extends BaseEntity {

    // 이벤트 구분
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false)
    private String eventKey;

    @Column(nullable = false)
    private Long memberId;

}
