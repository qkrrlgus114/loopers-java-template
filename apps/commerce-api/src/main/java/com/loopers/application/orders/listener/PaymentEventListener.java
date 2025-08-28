package com.loopers.application.orders.listener;

import com.loopers.application.orders.service.OrderFailService;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.application.payment.PaymentContext;
import com.loopers.application.payment.PaymentService;
import com.loopers.application.payment.processor.PaymentProcessorRegistry;
import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.event.StockDecrementedEvent;
import com.loopers.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OrdersService ordersService;
    private final OrderFailService orderFailService;
    private final PaymentService paymentService;
    private final PaymentProcessorRegistry paymentProcessorRegistry;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StockDecrementedEvent event) {
        try {

            // 1. 주문 정보 조회
            Orders orders = ordersService.findById(event.ordersId());

            // 2. 결제 엔티티 생성 및 저장
            Payment payment = paymentService.register(
                    orders.getId(),
                    orders.getOrderKey(),
                    event.paymentType(),
                    event.cardType(),
                    event.cardNo(),
                    orders.getTotalPrice(),
                    event.memberId()
            );

            // 3. 결제 프로세서에 넘길 컨텍스트 생성
            PaymentContext paymentContext = PaymentContext.of(
                    orders.getOrderKey(),
                    event.memberId(),
                    orders.getTotalPrice(),
                    event.paymentType(),
                    event.cardType(),
                    event.cardNo(),
                    payment.getId(),
                    event.couponId()
            );

            // 4. 결제 타입에 맞는 프로세서로 결제 실행
            paymentProcessorRegistry.get(event.paymentType()).processPayment(paymentContext);
        } catch (Exception e) {
            orderFailService.markFailed(event.ordersId());
            throw e;
        }
    }
}
