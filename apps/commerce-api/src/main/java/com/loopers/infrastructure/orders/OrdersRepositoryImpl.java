package com.loopers.infrastructure.orders;

import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepository {

    private final OrdersJpaRepository ordersJpaRepository;

    public Orders register(Orders orders) {
        return ordersJpaRepository.save(orders);
    }
}
