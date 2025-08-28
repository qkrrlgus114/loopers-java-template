package com.loopers.domain.orders.event;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;

public record CouponProcessedEvent(
        Long ordersId,
        Long memberId,
        Long couponId,
        PaymentType paymentType,
        CardType cardType,
        String cardNo) {

    public static CouponProcessedEvent from(OrdersCreatedEvent event) {
        return new CouponProcessedEvent(event.ordersId(), event.memberId(), event.couponId(), event.paymentType(), event.cardType(), event.cardNo());
    }
}
