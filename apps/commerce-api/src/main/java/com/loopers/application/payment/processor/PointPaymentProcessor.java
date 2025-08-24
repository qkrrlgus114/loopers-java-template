package com.loopers.application.payment.processor;

import com.loopers.application.payment.PaymentContext;
import com.loopers.application.payment.PaymentService;
import com.loopers.application.point.service.PointService;
import com.loopers.domain.payment.PaymentType;
import com.loopers.domain.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * 포인트 결제 전략 구현체
 * */
@Component
@RequiredArgsConstructor
public class PointPaymentProcessor implements PaymentProcessor {

    private final PointService pointService;
    private final PaymentService paymentService;


    @Override
    public PaymentType supports() {
        return PaymentType.POINT;
    }

    @Override
    public PaymentResult processPayment(PaymentContext paymentContext) {
        // 포인트 확인
        Point point = pointService.getPointByMemberIdWithLock(paymentContext.memberId());
        point.enoughPoint(paymentContext.amount());
        // 포인트 사용
        point.use(paymentContext.amount());

        return PaymentResult.of(
                "SUCCESS",
                "COMPLETED",
                null,
                null
        );
    }
}
