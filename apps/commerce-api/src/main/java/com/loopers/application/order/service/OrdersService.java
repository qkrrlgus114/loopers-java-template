package com.loopers.application.order.service;

import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;

    public Orders placeOrder(Long productId, Long memberId, int quantity, String orderKey) {
        Orders orders = Orders.create(memberId, productId, quantity, orderKey);

        return ordersRepository.register(orders);
    }

}
