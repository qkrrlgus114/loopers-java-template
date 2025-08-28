package com.loopers.application.orders.service;

import com.loopers.domain.orders.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFailService {
    private final OrdersService ordersService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long ordersId) {
        Orders orders = ordersService.findById(ordersId);
        orders.updateOrdersFailed();
    }
}
