package com.loopers.domain.orders;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = true)
    private Long couponMemberId;

    protected Orders() {
    }

    private Orders(Long memberId, int quantity, BigDecimal totalPrice, Long couponMemberId) {
        this.memberId = memberId;
        this.orderStatus = OrderStatus.PENDING;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.couponMemberId = couponMemberId;
    }

    public static Orders create(Long memberId, int quantity, BigDecimal totalPrice, Long couponMemberId, boolean couponUsed) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 가격은 0 이상이어야 합니다.");
        }
        if (couponUsed && (couponMemberId == null || couponMemberId <= 0)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "쿠폰 사용 시 유효한 쿠폰 회원 ID가 필요합니다.");
        }

        return new Orders(memberId, quantity, totalPrice, couponMemberId);
    }

    public Long getMemberId() {
        return memberId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Long getCouponMemberId() {
        return couponMemberId;
    }
}
