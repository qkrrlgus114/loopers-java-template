package com.loopers.application.payment.command;

public record PaymentCallbackCommand(
        String transactionKey,
        String orderId
) {

    public static PaymentCallbackCommand of(String transactionKey, String orderId) {
        return new PaymentCallbackCommand(transactionKey, orderId);
    }
}
