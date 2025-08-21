package com.loopers.application.product.result;

import com.loopers.domain.product.Product;

import java.math.BigDecimal;

public record ProductRegisterResult(
        Long productId,
        String name,
        String description,
        String brandId,
        String status,
        int likeCount,
        BigDecimal price
) {

    public static ProductRegisterResult of(Product product) {
        return new ProductRegisterResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                String.valueOf(product.getBrandId()),
                product.getStatus().name(),
                product.getLikeCount(),
                product.getPrice()
        );
    }
}
