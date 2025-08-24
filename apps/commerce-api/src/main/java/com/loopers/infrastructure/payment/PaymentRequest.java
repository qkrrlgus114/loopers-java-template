package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.CardType;

import java.math.BigDecimal;

public record PaymentRequest(
        String orderId,
        CardType cardType,
        String cardNo,
        String amount,
        String callbackUrl
) {

    private static final String CALLBACK_URL = "http://localhost:8080/api/v1/payments/callback";

    public static PaymentRequest of(String orderId, CardType cardType, String cardNo, BigDecimal amount) {
        return new PaymentRequest(orderId, cardType, cardNo, amount.toPlainString(), CALLBACK_URL);
    }
}
