package com.loopers.domain.point.history;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    private Long memberId;

    private Long pointId;

    private Integer amount;

    private String description;

    protected PointHistory() {
    }

    private PointHistory(Long memberId, Long pointId, Integer amount, String description) {
        this.memberId = memberId;
        this.pointId = pointId;
        this.amount = amount;
        this.description = description;
    }

    public static PointHistory create(Long memberId, Long pointId, Integer amount, PointHistoryStatus status) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
        }
        if (pointId == null || pointId <= 0) {
            throw new IllegalArgumentException("유효한 포인트 ID가 필요합니다.");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("유효한 금액이 필요합니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("포인트 히스토리 상태가 필요합니다.");
        }
        return new PointHistory(memberId, pointId, amount, status.getDescription());
    }

    public Long getMemberId() {
        return memberId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
