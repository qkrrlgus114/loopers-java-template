package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;

import java.math.BigDecimal;

public record PaymentContext(
        String orderKey,
        Long memberId,
        BigDecimal amount,
        PaymentType paymentType,
        CardType cardType,
        String cardNo,
        Long paymentId,
        Long couponId
) {

    public static PaymentContext of(String orderKey,
                                    Long memberId,
                                    BigDecimal amount,
                                    PaymentType paymentType,
                                    CardType cardType,
                                    String cardNo,
                                    Long paymentId,
                                    Long couponId) {
        if (orderKey == null || orderKey.isBlank()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 키가 비어 있습니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 금액입니다.");
        }
        if (!PaymentType.isValid(paymentType)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 결제 유형입니다: " + paymentType);
        }
        if (paymentType == PaymentType.CARD) {
            if (!CardType.isValid(cardType)) {
                throw new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 카드 유형입니다: " + cardType);
            }
            if (cardNo == null || cardNo.isBlank()) {
                throw new CoreException(CommonErrorType.BAD_REQUEST, "카드 번호가 비어 있습니다.");
            }
            if (!cardNo.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
                throw new CoreException(CommonErrorType.BAD_REQUEST, "카드 번호 형식이 잘못되었습니다: " + cardNo);
            }
        }
        if (paymentId != null && paymentId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 결제 ID입니다.");
        }
        return new PaymentContext(orderKey, memberId, amount, paymentType, cardType, cardNo, paymentId, couponId);
    }

}
