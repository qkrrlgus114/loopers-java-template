package com.loopers.application.recovery;

import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.payment.PaymentService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.stock.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentRecoveryService {
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;
    private final StockService stockService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void restoreStockForPayment(Long paymentId) {
        Payment payment = paymentService.findById(paymentId);

        OrderItem orderItem = orderItemService.findByOrderId(payment.getOrderId());

        // 락을 잡은 상태로 재고 가져옴.
        Stock stock = stockService.findStockByProductIdWithLock(orderItem.getProductId());

        stock.increaseQuantity(orderItem.getQuantity());
        payment.restoreStock();
    }
}
