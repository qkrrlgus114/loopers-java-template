package com.loopers.domain.orderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    void saveAll(List<OrderItem> orderItems);

    Optional<OrderItem> findByOrdersId(Long ordersId);
}
