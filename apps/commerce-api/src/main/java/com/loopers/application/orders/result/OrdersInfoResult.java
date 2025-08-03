package com.loopers.application.orders.result;

import com.loopers.domain.orders.OrderStatus;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersInfoResult {

    private OrderStatus status;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    private int totalCount;

    private OrdersInfoResult(OrderStatus status, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        this.status = status;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
    }

    public static OrdersInfoResult of(OrderStatus status, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        validate(status, orderDate, totalPrice, totalCount);

        return new OrdersInfoResult(status, orderDate, totalPrice, totalCount);
    }

    private static void validate(OrderStatus status, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        if (status == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 상태는 필수입니다.");
        }
        if (orderDate == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 날짜는 필수입니다.");
        }
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 금액은 0 이상이어야 합니다.");
        }
        if (totalCount < 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "총 수량은 0 이상이어야 합니다.");
        }
    }
}
