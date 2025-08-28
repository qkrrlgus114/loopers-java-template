package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.CardType;

public record PaymentRequest(
        String orderId,
        CardType cardType,
        String cardNo,
        Long amount,
        String callbackUrl
) {

    private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";

    public static PaymentRequest of(String orderId, CardType cardType, String cardNo, Long amount) {
        return new PaymentRequest(orderId, cardType, cardNo, amount, CALLBACK_URL);
    }
}
