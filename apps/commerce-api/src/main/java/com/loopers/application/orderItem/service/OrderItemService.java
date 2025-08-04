package com.loopers.application.orderItem.service;

import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public void register(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }
}
