package com.loopers.domain.orders;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = true)
    private Long couponMemberId;

    @Column(nullable = false)
    private String orderKey;

    protected Orders() {
    }

    private Orders(Long memberId, int quantity, BigDecimal totalPrice, String orderKey) {
        this.memberId = memberId;
        this.orderStatus = OrderStatus.PENDING;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderKey = orderKey;
    }

    public static Orders create(Long memberId, int quantity, BigDecimal totalPrice, String orderKey) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 가격은 0 이상이어야 합니다.");
        }
        if (orderKey == null || orderKey.isBlank()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 주문 키가 필요합니다.");
        }

        return new Orders(memberId, quantity, totalPrice, orderKey);
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

    public String getOrderKey() {
        return orderKey;
    }

    public void confirm() {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void fail() {
        this.orderStatus = OrderStatus.FAILED;
    }
}
