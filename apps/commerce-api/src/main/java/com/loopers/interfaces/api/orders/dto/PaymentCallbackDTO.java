package com.loopers.interfaces.api.orders.dto;


import java.math.BigDecimal;

public record PaymentCallbackDTO(
        String transactionKey,
        String orderId,
        String cardType,
        String cardNo,
        BigDecimal amount,
        String status,
        String reason
) {
}
