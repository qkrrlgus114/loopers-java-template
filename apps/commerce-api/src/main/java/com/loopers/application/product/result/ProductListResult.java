package com.loopers.application.product.result;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;

import java.math.BigDecimal;

public record ProductListResult(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        Integer likeCount
) {

    public static ProductListResult of(Long productId, String name, String description, BigDecimal price, ProductStatus status, Integer likeCount) {
        return new ProductListResult(productId, name, description, price, status, likeCount);
    }

    public static ProductListResult of(Product product) {
        return new ProductListResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getLikeCount()
        );
    }
}
