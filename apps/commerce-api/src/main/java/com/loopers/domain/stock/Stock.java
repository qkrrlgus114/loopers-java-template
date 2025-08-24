package com.loopers.domain.stock;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    protected Stock() {
    }

    private Stock(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Stock create(Long productId, int quantity) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 상품 ID가 필요합니다.");
        }
        if (quantity < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 0 이상이어야 합니다.");
        }
        return new Stock(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "감소할 수량은 1 이상이어야 합니다.");
        }
        if (this.quantity < quantity) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "재고가 부족합니다.");
        }
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "증가할 수량은 1 이상이어야 합니다.");
        }
        this.quantity += quantity;
    }

    public void enoughQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "확인할 수량은 1 이상이어야 합니다.");
        }
        if (this.quantity < quantity) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "재고가 부족합니다.");
        }
    }
}
