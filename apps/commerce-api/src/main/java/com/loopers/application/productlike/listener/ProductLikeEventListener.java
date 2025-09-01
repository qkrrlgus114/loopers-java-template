package com.loopers.application.productlike.listener;

import com.loopers.application.product.service.ProductService;
import com.loopers.domain.product.Product;
import com.loopers.domain.productlike.event.ProductLikedEvent;
import com.loopers.domain.productlike.event.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductLikeEventListener {

    private final ProductService productService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLike(ProductLikedEvent event) {
        try {
            log.info("좋아요 이벤트 리스너 실행 - productId: {}, memberId: {}", event.productId(), event.memberId());
            Product product = productService.findProductById(event.productId());

            product.increaseLikeCount();
            log.info("좋아요 이벤트 리스너 완료 - productId: {}, newLikeCount: {}", event.productId(), product.getLikeCount());
        } catch (Exception e) {
            log.error("좋아요 이벤트 리스너 처리 중 오류 발생 - productId: {}, memberId: {}", event.productId(), event.memberId(), e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUnlike(ProductUnlikedEvent event) {
        try {
            log.info("좋아요 취소 이벤트 리스너 실행 - productId: {}, memberId: {}", event.productId(), event.memberId());
            Product product = productService.findProductById(event.productId());

            product.decreaseLikeCount();
            log.info("좋아요 취소 이벤트 리스너 완료 - productId: {}, newLikeCount: {}", event.productId(), product.getLikeCount());
        } catch (Exception e) {
            log.error("좋아요 취소 이벤트 리스너 처리 중 오류 발생 - productId: {}, memberId: {}", event.productId(), event.memberId(), e);
        }
    }
}
