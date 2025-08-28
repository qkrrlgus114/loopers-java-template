package com.loopers.application.orders.listener;

import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.orders.service.OrderFailService;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orders.event.CouponProcessedEvent;
import com.loopers.domain.orders.event.StockDecrementedEvent;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockEventListener {

    private final StockService stockService;
    private final OrderFailService orderFailService;
    private final OrderItemService orderItemService;
    private final OrdersService ordersService;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CouponProcessedEvent event) {
        String orderKey = event.orderKey();
        log.info("[{}] 재고 차감 리스너 시작. event: {}", orderKey, event);

        try {
            log.info("[{}] 주문 아이템 조회 시작. orderId: {}", orderKey, event.ordersId());
            List<OrderItem> orderItems = orderItemService.findAllByOrdersId(event.ordersId());
            log.info("[{}] 주문 아이템 조회 완료. count: {}", orderKey, orderItems.size());

            for (OrderItem item : orderItems) {
                log.info("[{}] 상품 재고 차감 시도. productId: {}, quantity: {}", orderKey, item.getProductId(), item.getQuantity());
                Stock stock = stockService.findStockByProductIdWithLock(item.getProductId());
                stock.decreaseQuantity(item.getQuantity());
                log.info("[{}] 상품 재고 차감 완료. productId: {}", orderKey, item.getProductId());
            }

            StockDecrementedEvent nextEvent = StockDecrementedEvent.from(event);
            log.info("[{}] StockDecrementedEvent 발행 시작. event: {}", orderKey, nextEvent);
            eventPublisher.publishEvent(nextEvent);
            log.info("[{}] StockDecrementedEvent 발행 완료.", orderKey);

        } catch (Exception e) {
            log.error("[{}] 재고 차감 실패. event: {}", orderKey, event, e);
            orderFailService.markFailed(event.ordersId());
            throw e;
        }
    }

}
