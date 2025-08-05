package com.loopers.application.orders.service;

import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;

    /*
     * 주문 생성
     * */
    public Orders placeOrder(Long memberId, int quantity, BigDecimal totalPrice, Long couponMemberId, boolean couponUsed) {
        Orders orders = null;

        orders = Orders.create(memberId, quantity, totalPrice, couponMemberId, couponUsed);
        return ordersRepository.register(orders);
    }

}
