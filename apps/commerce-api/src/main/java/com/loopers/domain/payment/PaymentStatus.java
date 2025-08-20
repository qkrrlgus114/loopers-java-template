package com.loopers.domain.payment;

public enum PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING,
    CANCELLED;

    public static boolean isValid(PaymentStatus paymentStatus) {
        return paymentStatus == SUCCESS || paymentStatus == FAILED || paymentStatus == PENDING || paymentStatus == CANCELLED;
    }
}
