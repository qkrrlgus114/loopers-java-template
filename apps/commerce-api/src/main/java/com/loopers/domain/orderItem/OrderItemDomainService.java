package com.loopers.domain.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderItemDomainService {

    public OrderItem createOrderItem(
            Long orderId,
            Long productId,
            int quantity,
            BigDecimal price
    ) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("유효한 주문 ID가 필요합니다.");
        }
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효한 상품 ID가 필요합니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        return OrderItem.create(orderId, productId, quantity, price);
    }

}
