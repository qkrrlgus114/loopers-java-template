package com.loopers.application.productlike.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeCommand {

    private Long productId;

    private Long memberId;

    protected ProductLikeCommand(Long productId, Long memberId) {
        this.productId = productId;
        this.memberId = memberId;
    }

    public static ProductLikeCommand of(Long productId, Long memberId) {
        validate(productId, memberId);

        return new ProductLikeCommand(productId, memberId);
    }

    private static void validate(Long productId, Long memberId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효한 상품 ID가 필요합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
        }
    }
}
