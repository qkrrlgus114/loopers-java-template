package com.loopers.application.order.facade;

import com.loopers.application.order.command.PlaceOrderCommand;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.product.Product;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderFacade {

    /*
     * 1. 사용자가 주문을 넣는다.(productId, memberId, quantity 등)
     * 2. 상품을 찾는다.
     * 3. 재고를 확인한다. 충분?
     * 4. 재고를 차감한다.
     * 5. 주문을 생성한다.
     * 6. 주문 정보를 반환한다.
     * */

    private final ProductService productService;
    private final StockService stockService;

    @Transactional
    public void placeOrder(PlaceOrderCommand command) {
        Product product = productService.findProductById(command.getProductId());
        Stock stock = stockService.findStockByProductId(command.getProductId());

        // 재고 감소 로직
        stock.decreaseQuantity(command.getQuantity());


    }
}

