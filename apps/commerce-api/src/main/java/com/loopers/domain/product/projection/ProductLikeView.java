package com.loopers.domain.product.projection;

import com.loopers.domain.product.Product;


public record ProductLikeView(
        Product product,
        Long likeCount,
        Boolean likedByMe
) {
}

