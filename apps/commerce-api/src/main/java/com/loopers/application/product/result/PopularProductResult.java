package com.loopers.application.product.result;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;

import java.math.BigDecimal;

public record PopularProductResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        int likeCount,
        Double rankingScore,
        Long rank
) {

    public static PopularProductResult of(Product product, Double rankingScore, Long rank) {
        return new PopularProductResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getLikeCount(),
                rankingScore,
                rank
        );
    }

    public static PopularProductResult of(Product product) {
        return new PopularProductResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getLikeCount(),
                0.0,
                null
        );
    }
}