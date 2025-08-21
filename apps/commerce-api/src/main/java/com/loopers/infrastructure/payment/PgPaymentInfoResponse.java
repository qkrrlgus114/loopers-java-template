package com.loopers.infrastructure.payment;

import java.math.BigDecimal;

public record PgPaymentInfoResponse(
        Meta meta,
        Data data
) {
    public record Meta(String result) {
    }

    public record Data(
            String transactionKey,
            String orderId,
            String cardType,
            String cardNo,
            BigDecimal amount,
            String status,
            String reason
    ) {
    }
}
