package com.loopers.application.order.result;

import com.loopers.domain.orders.OrderStatus;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersInfoResult {

    private OrderStatus status;

    private LocalDateTime orderDate;

    private int totalAmount;

    private int totalCount;

    private String orderKey;

    private OrdersInfoResult(OrderStatus status, LocalDateTime orderDate, int totalAmount, int totalCount, String orderKey) {
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.totalCount = totalCount;
        this.orderKey = orderKey;
    }

    public static OrdersInfoResult of(OrderStatus status, LocalDateTime orderDate, int totalAmount, int totalCount, String orderKey) {
        validate(status, orderDate, totalAmount, totalCount, orderKey);

        return new OrdersInfoResult(status, orderDate, totalAmount, totalCount, orderKey);
    }

    private static void validate(OrderStatus status, LocalDateTime orderDate, int totalAmount, int totalCount, String orderKey) {
        if (status == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 상태는 필수입니다.");
        }
        if (orderDate == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 날짜는 필수입니다.");
        }
        if (totalAmount < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 금액은 0 이상이어야 합니다.");
        }
        if (totalCount < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 수량은 0 이상이어야 합니다.");
        }
        if (orderKey == null || orderKey.trim().isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 키는 필수입니다.");
        }
    }
}
