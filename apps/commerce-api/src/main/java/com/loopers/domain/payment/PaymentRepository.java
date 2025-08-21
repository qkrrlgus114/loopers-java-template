package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findById(Long paymentId);

    Optional<Long> findMemberIdByTransactionKey(String transactionKey);

    Optional<Payment> findByTransactionKey(String transactionKey);
}
