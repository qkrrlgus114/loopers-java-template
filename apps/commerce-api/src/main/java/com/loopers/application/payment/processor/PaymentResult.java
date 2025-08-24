package com.loopers.application.payment.processor;

public record PaymentResult
        (
                String result,
                String status,
                String reason,
                String transactionKey
        ) {

    public static PaymentResult of(String result, String status, String reason, String transactionKey) {
        return new PaymentResult(result, status, reason, transactionKey);
    }
}
