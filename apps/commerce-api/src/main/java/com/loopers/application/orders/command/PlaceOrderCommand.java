package com.loopers.application.orders.command;

import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOrderCommand {

    private Long memberId;
    private List<Item> items;
    private Long couponId;

    private PlaceOrderCommand(Long memberId, List<Item> items, Long couponId) {
        this.memberId = memberId;
        this.items = items;
        this.couponId = couponId;
    }

    public static PlaceOrderCommand of(Long memberId, List<Item> items, Long couponId) {
        validate(memberId, items, couponId);

        return new PlaceOrderCommand(memberId, items, couponId);
    }

    private static void validate(Long memberId, List<Item> items, Long couponId) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (items == null || items.isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 항목이 비어 있습니다.");
        }
        if (couponId != null && couponId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 쿠폰 ID입니다.");
        }
        items.forEach(item -> itemValidate(item));
    }

    public static void itemValidate(Item item) {
        if (item.getProductId() == null || item.getProductId() <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }
        if (item.getQuantity() <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
        }
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Item {

        private Long productId;

        private int quantity;

        private BigDecimal price;

        public Item(Long productId, int quantity, BigDecimal price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public static Item of(Long productId, int quantity, BigDecimal price) {
            return new Item(productId, quantity, price);
        }
    }

}
