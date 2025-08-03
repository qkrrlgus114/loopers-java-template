package com.loopers.application.orders.facade;

import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.result.OrdersInfoResult;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.application.point.service.PointService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemDomainService;
import com.loopers.domain.orders.OrderStatus;
import com.loopers.domain.orders.Orders;
import com.loopers.domain.point.Point;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersFacade {

    private final OrdersService ordersService;
    private final OrderItemDomainService orderItemDomainService;
    private final OrderItemService orderItemsService;
    private final PointService pointService;
    private final StockService stockService;

    @Transactional
    public OrdersInfoResult placeOrder(PlaceOrderCommand command) {
        BigDecimal totalQuantity = BigDecimal.valueOf(command.getItems().stream()
                .mapToInt(PlaceOrderCommand.Item::getQuantity)
                .sum());

        // 주문 생성
        Orders orders = ordersService.placeOrder(
                command.getMemberId(),
                totalQuantity.intValue()
        );

        // 주문 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (PlaceOrderCommand.Item item : command.getItems()) {
            OrderItem orderItem = orderItemDomainService.createOrderItem(
                    orders.getId(),
                    item.getProductId(),
                    item.getQuantity(),
                    BigDecimal.valueOf(item.getPrice())
            );
            orderItems.add(orderItem);
        }
        orderItemsService.register(orderItems);

        // 재고 감소
        for (OrderItem orderItem : orderItems) {
            Stock stock = stockService.findStockByProductId(orderItem.getProductId());
            stock.decreaseQuantity(orderItem.getQuantity());
        }

        // 포인트 확인 및 사용
        Point point = pointService.findPointByMemberId(command.getMemberId());
        point.use(command.getTotalPrice());

        return OrdersInfoResult.of(
                OrderStatus.PENDING,
                LocalDateTime.now(),
                command.getTotalPrice(),
                orderItems.size()
        );
    }
}

