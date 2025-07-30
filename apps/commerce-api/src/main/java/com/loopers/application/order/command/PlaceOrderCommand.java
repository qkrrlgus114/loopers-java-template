package com.loopers.application.order.command;

import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOrderCommand {

    private Long productId;

    private Long memberId;

    private int quantity;

    public PlaceOrderCommand(Long productId, Long memberId, int quantity) {
        this.productId = productId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public static PlaceOrderCommand of(Long productId, Long memberId, int quantity) {
        validate(productId, memberId, quantity);

        return new PlaceOrderCommand(productId, memberId, quantity);
    }

    private static void validate(Long productId, Long memberId, int quantity) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (quantity <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
    }
}
