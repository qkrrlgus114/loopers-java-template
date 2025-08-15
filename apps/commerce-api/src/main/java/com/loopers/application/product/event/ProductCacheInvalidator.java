package com.loopers.application.product.event;

import com.loopers.application.product.service.ProductChangeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductCacheInvalidator {

    private final StringRedisTemplate stringRedisTemplate;

    @TransactionalEventListener
    public void onProductChangeEvent(ProductChangeEvent event) {
        stringRedisTemplate.opsForValue().increment("products:v1:default:ver");
    }
}
