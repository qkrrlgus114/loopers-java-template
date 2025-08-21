package com.loopers.application.payment;

import com.loopers.application.payment.facade.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSchedule {

    private final PaymentFacade paymentFacade;

    // 10분마다 결제 실패 건 찾아서 재고 업데이트
    @Scheduled(fixedRate = 60 * 1000 * 10)
    public void updatePaymentInfo() {
        paymentFacade.searchFailedPaymentAndUpdateStock();
    }
}
