package com.loopers.application.productlike.producer;

import com.loopers.domain.productlike.event.ProductLikedEvent;
import com.loopers.domain.productlike.event.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductLikeProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProductLikedEvent(ProductLikedEvent event) {
        kafkaTemplate.send("product-like-topic", event);
    }

    public void sendProdcutUnlikedEvent(ProductUnlikedEvent event) {
        kafkaTemplate.send("product-unlike-topic", event);
    }
}
