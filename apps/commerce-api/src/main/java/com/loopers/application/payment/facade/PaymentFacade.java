package com.loopers.application.payment.facade;

import com.loopers.application.payment.PaymentService;
import com.loopers.application.payment.command.PaymentCallbackCommand;
import com.loopers.application.recovery.PaymentRecoveryService;
import com.loopers.domain.payment.Payment;
import com.loopers.infrastructure.payment.PgPaymentApiClient;
import com.loopers.infrastructure.payment.PgPaymentInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFacade {

    private final PgPaymentApiClient client;
    private final PaymentService paymentService;
    private final PaymentRecoveryService paymentRecoveryService;

    @Transactional
    public void processCallbackPayment(PaymentCallbackCommand command) {
        // 트랜잭션키로 사용자 ID 조회
        Long memberId = paymentService.findMemberIdByTransactionKey(command.transactionKey());
        // 트랜잭션키로 결제 정보 조회
        PgPaymentInfoResponse paymentInfo = client.getPaymentInfo(command.transactionKey(), memberId);

        // 결제 정보 업데이트
        paymentService.updatePaymentInfo(paymentInfo);
    }

    public void searchFailedPaymentAndUpdateStock() {
        List<Payment> paymentList = paymentService.findByFailedPaymentStatus();

        for (Payment payment : paymentList) {
            try {
                paymentRecoveryService.restoreStockForPayment(payment.getId());
            } catch (Exception e) {
                log.error("결제 실패 건 처리 중 오류 | paymentId={}, err={}",
                        payment.getId(), e.toString());
            }
        }
    }

    public void updatePendingPayments() {
        // 결제 대기 건 전부 조회
        List<Payment> pendingPayments = paymentService.findByPendingPaymentStatus();

        for (Payment payment : pendingPayments) {
            try {
                // 결제 정보 조회
                PgPaymentInfoResponse paymentInfo = client.getPaymentInfo(payment.getTransactionKey(), payment.getMemberId());

                paymentService.updatePaymentInfoTransactional(paymentInfo);
            } catch (Exception e) {
                log.error("결제 대기 건 처리 중 오류 발생 | paymentId={}, error={}", payment.getId(), e.getMessage());
            }
        }
    }
}
