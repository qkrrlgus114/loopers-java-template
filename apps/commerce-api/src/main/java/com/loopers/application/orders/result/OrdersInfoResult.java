package com.loopers.application.orders.result;

import com.loopers.domain.orders.OrderStatus;

import java.math.BigDecimal;

public record OrdersInfoResult(
        Long ordersId,
        OrderStatus status,
        int quantity,
        BigDecimal totalPrice,
        Long CouponMemberId
) {

    public static OrdersInfoResult of(Long ordersId, OrderStatus status, int quantity, BigDecimal totalPrice, Long couponMemberId) {
        return new OrdersInfoResult(ordersId, status, quantity, totalPrice, couponMemberId);
    }

}
