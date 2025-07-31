package com.loopers.application.productlike.result;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeResult {

    private Long productId;

    private boolean isLiked;

    private Integer likeCount;

    private boolean status;

    protected ProductLikeResult(Long productId, boolean isLiked, Integer likeCount, boolean status) {
        this.productId = productId;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.status = status;
    }

    public static ProductLikeResult of(Long productId, boolean isLiked, Integer likeCount, boolean status) {
        validate(productId, likeCount);

        return new ProductLikeResult(productId, isLiked, likeCount, status);
    }

    private static void validate(Long productId, Integer likeCount) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효한 상품 ID가 필요합니다.");
        }
        if (likeCount == null || likeCount < 0) {
            throw new IllegalArgumentException("상품의 전체 좋아요 수는 0 이상이어야 합니다.");
        }
    }


}
