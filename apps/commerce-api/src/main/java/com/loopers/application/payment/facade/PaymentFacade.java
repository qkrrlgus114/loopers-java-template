package com.loopers.application.payment.facade;

import com.loopers.application.payment.PaymentService;
import com.loopers.application.payment.command.PaymentCallbackCommand;
import com.loopers.infrastructure.payment.PgPaymentApiClient;
import com.loopers.infrastructure.payment.PgPaymentInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PgPaymentApiClient client;
    private final PaymentService paymentService;

    @Transactional
    public void processCallbackPayment(PaymentCallbackCommand command) {
        // 트랜잭션키로 사용자 ID 조회
        Long memberId = paymentService.findMemberIdByTransactionKey(command.transactionKey());
        // 트랜잭션키로 결제 정보 조회
        PgPaymentInfoResponse paymentInfo = client.getPaymentInfo(command.transactionKey(), memberId);

        // 결제 정보 업데이트
        paymentService.updatePaymentInfo(paymentInfo);
    }
}
