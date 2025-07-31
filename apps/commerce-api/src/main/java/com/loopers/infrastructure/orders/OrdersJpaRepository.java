package com.loopers.infrastructure.orders;

import com.loopers.domain.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersJpaRepository extends JpaRepository<Orders, Long> {
}
