package com.loopers.application.payment.processor;

import com.loopers.application.payment.PaymentContext;
import com.loopers.application.payment.PaymentGatewayPort;
import com.loopers.application.payment.PaymentService;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentType;
import com.loopers.infrastructure.payment.PgPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
 * 카드 결제 전략 구현체
 * */
@Component
@RequiredArgsConstructor
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    private final PaymentGatewayPort paymentGatewayPort;
    private final PaymentService paymentService;

    @Override
    public PaymentType supports() {
        return PaymentType.CARD;
    }

    @Override
    public PaymentResult processPayment(PaymentContext paymentContext) {
        PgPaymentResponse pgPaymentResponse = paymentGatewayPort.requestPayment(
                paymentContext.orderKey(),
                paymentContext.cardType(),
                paymentContext.cardNo(),
                paymentContext.amount(),
                paymentContext.memberId()
        );

        Payment payment = paymentService.findById(paymentContext.paymentId());

        // FAIL이 떨어지는 경우
        if (pgPaymentResponse != null && !pgPaymentResponse.meta().result().equals("SUCCESS")) {
            log.error("PG 결제 실패 | orderKey={}, memberId={}, error={}",
                    paymentContext.orderKey(), paymentContext.memberId(), pgPaymentResponse.data().reason());

            // 실패 상태 + 사유 업데이트
            payment.updateStatus(PaymentStatus.FAILED, pgPaymentResponse.data().reason());

            return PaymentResult.of(
                    pgPaymentResponse.meta().result(),
                    pgPaymentResponse.data().status(),
                    pgPaymentResponse.data().reason(),
                    null
            );
        }

        // 페이먼트 트랜잭션키 업데이트
        payment.updateTransactionKey(pgPaymentResponse.data().transactionKey());

        return PaymentResult.of(
                pgPaymentResponse.meta().result(),
                pgPaymentResponse.data().status(),
                pgPaymentResponse.data().reason(),
                pgPaymentResponse.data().transactionKey()
        );
    }
}
