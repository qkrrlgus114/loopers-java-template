package com.loopers.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@Table(name = "product_metric_daily")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDailyMetric extends BaseEntity {

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long saleQuantity;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false, updatable = false)
    private Long productId;

    @Builder
    private ProductDailyMetric(
            @Nullable Long likeCount,
            @Nullable Long saleQuantity,
            @Nullable Long viewCount,
            Long productId
    ) {
        if (viewCount != null && viewCount < 0) {
            throw new IllegalArgumentException("상품 조회 수가 올바르지 않습니다.");
        }
        if (productId == null) {
            throw new IllegalArgumentException("상품 아이디가 올바르지 않습니다.");
        }

        this.likeCount = Objects.requireNonNullElse(likeCount, 0L);
        this.saleQuantity = Objects.requireNonNullElse(saleQuantity, 0L);
        this.viewCount = Objects.requireNonNullElse(viewCount, 0L);
        this.productId = productId;
    }

}
