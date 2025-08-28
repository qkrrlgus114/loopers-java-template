package com.loopers.domain.orders.event;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;

public record StockDecrementedEvent(
        Long ordersId,
        String orderKey,
        Long memberId,
        Long couponId,
        PaymentType paymentType,
        CardType cardType,
        String cardNo) {

    public static StockDecrementedEvent from(CouponProcessedEvent event) {
        return new StockDecrementedEvent(event.ordersId(), event.orderKey(), event.memberId(), event.couponId(), event.paymentType(), event.cardType(), event.cardNo());
    }
}
