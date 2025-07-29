package com.loopers.application.product.result;

import com.loopers.domain.brand.BrandModel;
import com.loopers.domain.product.ProductModel;

import java.math.BigDecimal;

public record ProductDetailResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long memberId,
        Long brandId,
        String brandName,
        int likeCount
) {

    public static ProductDetailResult of(ProductModel product, BrandModel brand, int likeCount) {
        return new ProductDetailResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMemberId(),
                brand.getId(),
                brand.getName(),
                likeCount
        );
    }

}
