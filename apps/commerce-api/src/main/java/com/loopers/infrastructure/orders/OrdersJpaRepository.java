package com.loopers.infrastructure.orders;

import com.loopers.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByMemberId(Long memberId);
}
