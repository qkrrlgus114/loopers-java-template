package com.loopers.infrastructure.orderItem;

import com.loopers.domain.orderItem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.ordersId = :ordersId")
    Optional<OrderItem> findByOrdersId(Long ordersId);
}
