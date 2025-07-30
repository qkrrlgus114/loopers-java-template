package com.loopers.domain.stock;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class StockModel extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    protected StockModel() {
    }

    public StockModel(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static StockModel create(Long productId, int quantity) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 상품 ID가 필요합니다.");
        }
        if (quantity < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 0 이상이어야 합니다.");
        }
        return new StockModel(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
