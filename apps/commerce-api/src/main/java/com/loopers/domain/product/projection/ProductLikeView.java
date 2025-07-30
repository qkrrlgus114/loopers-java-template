package com.loopers.domain.product.projection;

import com.loopers.domain.product.ProductModel;


public record ProductLikeView(
        ProductModel product,
        Long likeCount,
        Boolean likedByMe
) {
}

