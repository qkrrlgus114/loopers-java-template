package com.loopers.application.payment.processor;

import com.loopers.application.payment.PaymentContext;
import com.loopers.domain.payment.PaymentType;

/*
 * 결제 타입을 결정하기 위한 전략 인터페이스
 * */
public interface PaymentProcessor {

    PaymentType supports();

    PaymentResult processPayment(PaymentContext paymentContext);

}
