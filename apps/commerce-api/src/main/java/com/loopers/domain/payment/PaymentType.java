package com.loopers.domain.payment;

public enum PaymentType {
    CARD, POINT;

    public static boolean isValid(PaymentType paymentType) {
        return paymentType == CARD || paymentType == POINT;
    }
}
