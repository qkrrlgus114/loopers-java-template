package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Column(nullable = true)
    private String transactionKey;

    @Column(nullable = false)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CardType cardType;

    @Column(nullable = true)
    private String cardNo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = true, length = 500)
    private String reason;

    protected Payment() {
    }

    private Payment(String orderId, PaymentType paymentType, CardType cardType, String cardNo, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentType = paymentType;
        this.cardType = cardType;
        this.cardNo = cardNo;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(String orderId, PaymentType paymentType, CardType cardType, String cardNo, BigDecimal amount) {
        isValid(orderId, paymentType, cardType, cardNo, amount);

        return new Payment(orderId, paymentType, cardType, cardNo, amount);
    }

    public static void isValid(String orderId, PaymentType paymentType, CardType cardType, String cardNo, BigDecimal amount) {
        if (orderId == null || orderId.isBlank()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 주문 ID가 필요합니다.");
        }
        if (paymentType == null || !PaymentType.isValid(paymentType)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 결제 유형이 필요합니다.");
        }
        if (cardType == null || !CardType.isValid(cardType)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 카드 유형이 필요합니다.");
        }
        if (cardNo == null || cardNo.isBlank()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 카드 번호가 필요합니다.");
        }
        if (!cardNo.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "카드 번호는 xxxx-xxxx-xxxx-xxxx 형식이어야 합니다.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 금액이 필요합니다.");
        }

    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public void updateTransactionKey(String transactionKey) {
        if (transactionKey == null || transactionKey.isBlank()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 트랜잭션 키가 필요합니다.");
        }
        this.transactionKey = transactionKey;
    }

    public void updateStatus(PaymentStatus status, String reason) {
        if (status == null || !PaymentStatus.isValid(status)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 결제 상태가 필요합니다.");
        }
        this.status = status;
        this.reason = reason;
    }
}
