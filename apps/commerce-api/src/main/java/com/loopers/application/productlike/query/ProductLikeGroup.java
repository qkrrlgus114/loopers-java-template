package com.loopers.application.productlike.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeGroup {

    private Long productId;

    private Integer likeCount;


    public ProductLikeGroup(Long productId, Long likeCnt) {
        this.productId = productId;
        this.likeCount = likeCnt.intValue();
    }
}
