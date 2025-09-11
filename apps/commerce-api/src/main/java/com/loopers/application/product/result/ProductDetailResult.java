package com.loopers.application.product.result;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;

import java.math.BigDecimal;

public record ProductDetailResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long memberId,
        Long brandId,
        String brandName,
        ProductStatus status,
        int likeCount,
        Boolean isLiked,
        Long rank
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
                product.getStatus(),
                product.getLikeCount(),
                likedByMember,
                null
        );
    }

    public static ProductDetailResult of(Product product, Brand brand, boolean likedByMember, Long rank) {
        return new ProductDetailResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMemberId(),
                product.getBrandId(),
                brand.getName(),
                product.getStatus(),
                product.getLikeCount(),
                likedByMember,
                rank
        );
    }

}
