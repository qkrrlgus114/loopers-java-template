package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.infrastructure.payment.PgPaymentResponse;

import java.math.BigDecimal;

public interface PaymentGatewayPort {

    PgPaymentResponse requestPayment(String orderKey,
                                     CardType cardType,
                                     String cardNo,
                                     BigDecimal amount,
                                     Long memberId);

}
