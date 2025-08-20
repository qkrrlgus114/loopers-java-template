package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.PaymentType;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /*
     * 결제 Entity 등록
     * */
    public Payment register(String orderKey,
                            PaymentType paymentType,
                            CardType cardType,
                            String cardNo,
                            BigDecimal amount) {

        Payment payment = Payment.create(orderKey, paymentType, cardType, cardNo, amount);

        return paymentRepository.save(payment);
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "결제 정보를 찾을 수 없습니다. paymentId: " + paymentId));
    }
}
