package com.loopers.application.orders.listener;

import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orders.event.CouponProcessedEvent;
import com.loopers.domain.orders.event.StockDecrementedEvent;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockEventListener {

    private final StockService stockService;
    private final OrderItemService orderItemService;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CouponProcessedEvent event) {
        List<OrderItem> orderItems = orderItemService.findAllByOrdersId(event.ordersId());

        for (OrderItem item : orderItems) {
            Stock stock = stockService.findStockByProductIdWithLock(item.getProductId());

            stock.decreaseQuantity(item.getQuantity());
        }
            
        eventPublisher.publishEvent(StockDecrementedEvent.from(event));
    }
}
