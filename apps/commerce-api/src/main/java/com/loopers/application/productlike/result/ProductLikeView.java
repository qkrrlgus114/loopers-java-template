package com.loopers.application.productlike.result;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeView {

    private Long productId;

    private String productName;

    private Integer likeCount;


    protected ProductLikeView(Long productId, String productName, Integer likeCount) {
        this.productId = productId;
        this.likeCount = likeCount;
        this.productName = productName;
    }

    public static ProductLikeView of(Long productId, String productName, Integer likeCount) {
        validate(productId, productName, likeCount);

        return new ProductLikeView(productId, productName, likeCount);
    }

    private static void validate(Long productId, String productName, Integer likeCount) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효한 상품 ID가 필요합니다.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 비어있을 수 없습니다.");
        }
        if (likeCount == null || likeCount < 0) {
            throw new IllegalArgumentException("상품의 전체 좋아요 수는 0 이상이어야 합니다.");
        }
    }


}
