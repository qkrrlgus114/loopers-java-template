package com.loopers.domain.orders.event;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;

public record OrdersCreatedEvent
        (Long ordersId,
         String orderKey,
         Long memberId,
         Long couponId,
         PaymentType paymentType,
         CardType cardType,
         String cardNo) {

    public static OrdersCreatedEvent of(Long ordersId, String orderKey, Long memberId, Long couponId, PaymentType paymentType, CardType cardType, String cardNo) {
        return new OrdersCreatedEvent(ordersId, orderKey, memberId, couponId, paymentType, cardType, cardNo);
    }
}
