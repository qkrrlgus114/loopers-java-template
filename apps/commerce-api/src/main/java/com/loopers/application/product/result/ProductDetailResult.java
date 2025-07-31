package com.loopers.application.product.result;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.projection.ProductLikeView;

import java.math.BigDecimal;

public record ProductDetailResult(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long memberId,
        Long brandId,
        String brandName,
        Long likeCount,
        Boolean isLiked
) {

    public static ProductDetailResult of(ProductLikeView productLikeView, Brand brand) {
        return new ProductDetailResult(
                productLikeView.product().getId(),
                productLikeView.product().getName(),
                productLikeView.product().getDescription(),
                productLikeView.product().getPrice(),
                productLikeView.product().getMemberId(),
                brand.getId(),
                brand.getName(),
                productLikeView.likeCount(),
                productLikeView.likedByMe()
        );
    }

}
