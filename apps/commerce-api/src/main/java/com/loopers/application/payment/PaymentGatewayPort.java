package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.infrastructure.payment.PgPaymentResponse;

public interface PaymentGatewayPort {

    PgPaymentResponse requestPayment(String orderKey,
                                     CardType cardType,
                                     String cardNo,
                                     Long amount,
                                     Long memberId);

}
