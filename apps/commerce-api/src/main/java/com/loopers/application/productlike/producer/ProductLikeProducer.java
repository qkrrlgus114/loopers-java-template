package com.loopers.application.productlike.producer;

import com.loopers.application.event.productlike.ProductLikedEvent;
import com.loopers.application.event.productlike.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductLikeProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendProductLikedEvent(ProductLikedEvent event) {
        kafkaTemplate.send("product-like-topic", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("카프카 메시지 전송 성공: {}", result.getRecordMetadata().toString());
                    } else {
                        log.error("카프카 메시지 전송 실패: {}", ex.getMessage());
                    }
                });
    }

    public void sendProdcutUnlikedEvent(ProductUnlikedEvent event) {
        kafkaTemplate.send("product-unlike-topic", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("카프카 메시지 전송 성공: {}", result.getRecordMetadata().toString());
                    } else {
                        log.error("카프카 메시지 전송 실패: {}", ex.getMessage());
                    }
                });
        ;
    }
}
