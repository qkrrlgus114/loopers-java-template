package com.loopers.infrastructure.orders;

import com.loopers.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByMemberId(Long memberId);

    @Query("SELECT o FROM Orders o WHERE o.orderKey = :orderKey")
    Optional<Orders> findByOrderKey(String orderKey);
}
