package com.loopers.application.product.result;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;

import java.math.BigDecimal;

public record ProductDetailResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long memberId,
        Long brandId,
        String brandName,
        int likeCount,
        Boolean isLiked
) {

    public static ProductDetailResult of(Product product, Brand brand, boolean likedByMember) {
        return new ProductDetailResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMemberId(),
                product.getBrandId(),
                brand.getName(),
                product.getLikeCount(),
                likedByMember
        );
    }

}
