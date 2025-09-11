package com.loopers.interfaces.consumer;

import com.loopers.config.kafka.KafkaConfig;
import com.loopers.domain.product.ProductLike;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductLikeConsumer {

    private final JdbcTemplate jdbcTemplate;

    // 상품 좋아요를 처리하는 컨슈머~!
    @KafkaListener(
            topics = "${loopers.kafka.topics.product-like}",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void productLikeConsumer(
            List<ProductLike> messages,
            Acknowledgment acknowledgment
    ) {
        for (ProductLike productLike : messages) {
            jdbcTemplate.update(
                    "UPDATE product SET like_count = like_count + 1 WHERE id = ?",
                    productLike.getProductId()
            );
            log.info("상품 좋아요 증가 처리 완료: {}", productLike.getProductId());


        }
        acknowledgment.acknowledge();
    }

    // 상품 싫어요를 처리하는 컨슈머~!
    @KafkaListener(
            topics = "${loopers.kafka.topics.product-unlike}",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void productUnlikeConsumer(
            List<ProductLike> messages,
            Acknowledgment acknowledgment
    ) {
        for (ProductLike productLike : messages) {
            jdbcTemplate.update(
                    "UPDATE product SET like_count = like_count - 1 WHERE id = ? AND like_count > 0",
                    productLike.getProductId()
            );
            log.info("상품 좋아요 감소 처리 완료: {}", productLike.getProductId());
        }
        acknowledgment.acknowledge();
    }
}
