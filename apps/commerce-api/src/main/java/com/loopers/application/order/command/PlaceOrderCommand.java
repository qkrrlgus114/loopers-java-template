package com.loopers.application.order.command;

import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOrderCommand {

    private Long memberId;
    private List<OrderItem> items;

    private PlaceOrderCommand(Long memberId, List<OrderItem> items) {
        this.memberId = memberId;
        this.items = items;
    }

    public static PlaceOrderCommand of(Long memberId, List<OrderItem> items) {
        validate(memberId, items);

        return new PlaceOrderCommand(memberId, items);
    }

    private static void validate(Long memberId, List<OrderItem> items) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (items == null || items.isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 항목이 비어 있습니다.");
        }
        items.forEach(OrderItem::validate);
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(i -> i.getPrice() * i.getQuantity())
                .sum();
    }
}
