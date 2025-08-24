package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p.memberId FROM Payment p WHERE p.transactionKey = :transactionKey")
    Optional<Long> findMemberIdByTransactionKey(String transactionKey);

    @Query("SELECT p FROM Payment p WHERE p.transactionKey = :transactionKey")
    Optional<Payment> findByTransactionKey(String transactionKey);

    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' and p.restoredStatus = false ")
    List<Payment> findByFailedPaymentStatus();

    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING'")
    List<Payment> findByPendingPaymentStatus();
}
