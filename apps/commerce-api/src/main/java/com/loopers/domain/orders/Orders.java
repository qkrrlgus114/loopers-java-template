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
    private Long productId;

    @Column(nullable = false)
    private String orderKey;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int quantity;

    protected Orders() {
    }

    private Orders(Long memberId, Long productId, int quantity, String orderKey) {
        this.memberId = memberId;
        this.productId = productId;
        this.orderStatus = OrderStatus.PENDING;
        this.quantity = quantity;
        this.orderKey = orderKey;
    }

    public static Orders create(Long memberId, Long productId, int quantity, String orderKey) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 상품 ID가 필요합니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (orderKey == null || orderKey.trim().isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 키가 필요합니다.");
        }

        return new Orders(memberId, productId, quantity, orderKey);
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOrderKey() {
        return orderKey;
    }
}
