package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private BigDecimal amount;

    protected Point() {
    }

    private Point(Long memberId, BigDecimal amount) {
        this.memberId = memberId;
        this.amount = amount;
    }

    public static Point create(Long memberId, BigDecimal amount) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "포인트는 0 이상이어야 합니다.");
        }
        return new Point(memberId, amount);
    }

    public static Point initCreate(Long id) {
        if (id == null || id <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        return new Point(id, BigDecimal.ZERO);
    }

    public Long getMemberId() {
        return memberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void use(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "사용할 포인트는 0 이상이어야 합니다.");
        }
        if (this.amount.compareTo(amount) < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }

        this.amount = this.amount.subtract(amount);
    }

    public void charge(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "충전할 포인트는 0 이상이어야 합니다.");
        }
        this.amount = this.amount.add(amount);
    }

    public void enoughPoint(BigDecimal subtract) {
        if (subtract == null || subtract.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "확인할 포인트는 0 이상이어야 합니다.");
        }
        if (this.amount.compareTo(subtract) < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "포인트가 부족합니다.");
        }
    }
}
