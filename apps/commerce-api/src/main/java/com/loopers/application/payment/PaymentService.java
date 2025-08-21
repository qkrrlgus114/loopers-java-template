package com.loopers.application.payment;

import com.loopers.domain.payment.*;
import com.loopers.infrastructure.payment.PgPaymentInfoResponse;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /*
     * 결제 Entity 등록
     * */
    public Payment register(
            Long orderId,
            String orderKey,
            PaymentType paymentType,
            CardType cardType,
            String cardNo,
            BigDecimal amount,
            Long memberId) {

        Payment payment = Payment.create(orderId, orderKey, paymentType, cardType, cardNo, amount, memberId);

        return paymentRepository.save(payment);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "결제 정보를 찾을 수 없습니다. paymentId: " + paymentId));
    }

    public Long findMemberIdByTransactionKey(String transactionKey) {
        return paymentRepository.findMemberIdByTransactionKey(transactionKey)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "트랜잭션 키로 회원 ID를 찾을 수 없습니다. transactionKey: " + transactionKey));
    }

    public void updatePaymentInfo(PgPaymentInfoResponse paymentInfo) {
        Payment payment = paymentRepository.findByTransactionKey(paymentInfo.data().transactionKey())
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "트랜잭션 키로 결제 정보를 찾을 수 없습니다. transactionKey: " + paymentInfo.data().transactionKey()));

        PaymentStatus paymentStatus = PaymentStatus.fromString(paymentInfo.data().status());
        payment.updateStatus(paymentStatus, paymentInfo.data().reason());
    }

    public List<Payment> findByFailedPaymentStatus() {
        return paymentRepository.findByFailedPaymentStatus();
    }


    public List<Payment> findByPendingPaymentStatus() {
        return paymentRepository.findByPendingPaymentStatus();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePaymentInfoTransactional(PgPaymentInfoResponse paymentInfo) {
        // 결제 정보 업데이트
        this.updatePaymentInfo(paymentInfo);
    }
}

