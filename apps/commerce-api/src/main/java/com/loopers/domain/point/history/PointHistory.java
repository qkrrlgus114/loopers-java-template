package com.loopers.domain.point.history;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long pointId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PointHistoryStatus status;

    protected PointHistory() {
    }

    private PointHistory(Long memberId, Long pointId, BigDecimal amount, PointHistoryStatus status) {
        this.memberId = memberId;
        this.pointId = pointId;
        this.amount = amount;
        this.status = status;
    }

    public static PointHistory create(Long memberId, Long pointId, BigDecimal amount, PointHistoryStatus status) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
        }
        if (pointId == null || pointId <= 0) {
            throw new IllegalArgumentException("유효한 포인트 ID가 필요합니다.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("유효한 금액이 필요합니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("포인트 히스토리 상태가 필요합니다.");
        }
        return new PointHistory(memberId, pointId, amount, status);
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPointId() {
        return pointId;
    }

    public PointHistoryStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
