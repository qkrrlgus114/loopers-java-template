package com.loopers.domain.orderItem;

import java.util.List;

public interface OrderItemRepository {
    void saveAll(List<OrderItem> orderItems);
}
