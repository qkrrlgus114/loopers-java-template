package com.loopers.application.productlike.listener;

import com.loopers.application.product.service.ProductService;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.event.ProductLikedEvent;
import com.loopers.domain.productlike.event.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductLikeEventListener {

    private final ProductService productService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLike(ProductLikedEvent event) {
        Product product = productService.findProductById(event.productId());

        product.increaseLikeCount();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUnlike(ProductUnlikedEvent event) {
        Product product = productService.findProductById(event.productId());

        product.decreaseLikeCount();
    }
}
