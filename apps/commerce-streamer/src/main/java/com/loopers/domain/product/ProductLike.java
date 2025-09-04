package com.loopers.domain.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductLike {
    private Long productId;
    private Long memberId;
}
