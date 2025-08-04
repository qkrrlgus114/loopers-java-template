package com.loopers.domain.orders;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Orders extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int quantity;

    protected Orders() {
    }

    private Orders(Long memberId, int quantity) {
        this.memberId = memberId;
        this.orderStatus = OrderStatus.PENDING;
        this.quantity = quantity;
    }

    public static Orders create(Long memberId, int quantity) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }

        return new Orders(memberId, quantity);
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
}
