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

    private PlaceOrderCommand(Long memberId, List<Item> items) {
        this.memberId = memberId;
        this.items = items;
    }

    public static PlaceOrderCommand of(Long memberId, List<Item> items) {
        validate(memberId, items);

        return new PlaceOrderCommand(memberId, items);
    }

    private static void validate(Long memberId, List<Item> items) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (items == null || items.isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 항목이 비어 있습니다.");
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
        if (item.getPrice() < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
        }
    }

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(items.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Item {

        private Long productId;

        private int quantity;

        private int price;

        public Item(Long productId, int quantity, int price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public static Item of(Long productId, int quantity, int price) {
            return new Item(productId, quantity, price);
        }
    }

}
