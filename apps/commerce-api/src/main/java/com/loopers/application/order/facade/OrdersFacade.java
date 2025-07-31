package com.loopers.application.order.facade;

import com.loopers.application.order.command.OrderItem;
import com.loopers.application.order.command.PlaceOrderCommand;
import com.loopers.application.order.result.OrdersInfoResult;
import com.loopers.application.order.service.OrdersService;
import com.loopers.application.point.service.PointService;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.orders.OrderStatus;
import com.loopers.domain.orders.Orders;
import com.loopers.domain.point.Point;
import com.loopers.domain.product.Product;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersFacade {

    /*
     * 1. 사용자가 주문을 넣는다.(productId, memberId, quantity 등)
     * 2. 사용자의 포인트 확인
     * 2. 상품을 찾는다.
     * 3. 재고를 확인한다. 충분?
     * 4. 재고를 차감한다.
     * 5. 주문을 생성한다.
     * 6. 주문 정보를 반환한다.
     * */

    private final ProductService productService;
    private final StockService stockService;
    private final OrdersService ordersService;
    private final PointService pointService;

    @Transactional
    public OrdersInfoResult placeOrder(PlaceOrderCommand command) {
        // 포인트 확인 및 사용
        Point point = pointService.findPointByMemberId(command.getMemberId());
        point.use(BigDecimal.valueOf(command.getTotalPrice()));

        // 주문을 통합할 키
        String orderKey = UUID.randomUUID().toString();

        int totalQuantity = command.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        // 상품 및 재고 확인
        for (OrderItem orderItem : command.getItems()) {
            Product product = productService.findProductById(orderItem.getProductId());
            Stock stock = stockService.findStockByProductId(orderItem.getProductId());

            // 재고 감소 로직
            stock.decreaseQuantity(orderItem.getQuantity());

            // 주문 생성
            Orders orders = ordersService.placeOrder(
                    orderItem.getProductId(),
                    command.getMemberId(),
                    orderItem.getQuantity(),
                    orderKey);
        }


        return OrdersInfoResult.of(
                OrderStatus.PENDING,
                LocalDateTime.now(),
                command.getTotalPrice(),
                totalQuantity,
                orderKey
        );
    }
}

