package com.loopers.domain.payment;

import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;

public enum PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING,
    CANCELLED;

    public static boolean isValid(PaymentStatus paymentStatus) {
        return paymentStatus == SUCCESS || paymentStatus == FAILED || paymentStatus == PENDING || paymentStatus == CANCELLED;
    }

    public static PaymentStatus fromString(String status) {
        try {
            return PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 결제 상태입니다: " + status);
        }
    }
}
