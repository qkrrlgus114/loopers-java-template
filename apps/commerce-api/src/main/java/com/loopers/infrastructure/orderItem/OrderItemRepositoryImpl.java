package com.loopers.infrastructure.orderItem;

import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public void saveAll(List<OrderItem> orderItems) {
        orderItemJpaRepository.saveAll(orderItems);
    }

    @Override
    public Optional<OrderItem> findByOrdersId(Long orderId) {
        return orderItemJpaRepository.findByOrdersId(orderId);
    }
}
