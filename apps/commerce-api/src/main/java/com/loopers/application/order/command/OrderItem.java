package com.loopers.application.order.command;

import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    private Long productId;

    private int quantity;

    private int price;

    private OrderItem(Long productId, int quantity, int price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem of(Long productId, int quantity, int price) {
        OrderItem orderItem = new OrderItem(productId, quantity, price);
        orderItem.validate();
        return orderItem;
    }

    public void validate() {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (price <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "가격은 1 이상이어야 합니다.");
        }
    }
}
