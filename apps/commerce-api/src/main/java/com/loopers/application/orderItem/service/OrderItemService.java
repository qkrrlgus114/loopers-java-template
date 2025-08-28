package com.loopers.application.orderItem.service;

import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
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

    public OrderItem findByOrderId(Long orderId) {
        return orderItemRepository.findByOrdersId(orderId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "주문 아이템을 찾을 수 없습니다. orderId: " + orderId));
    }

    public List<OrderItem> findAllByOrdersId(Long ordersId) {
        return orderItemRepository.findByOrdersId(ordersId)
                .map(List::of)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "주문 아이템을 찾을 수 없습니다. orderId: " + ordersId));
    }
}
