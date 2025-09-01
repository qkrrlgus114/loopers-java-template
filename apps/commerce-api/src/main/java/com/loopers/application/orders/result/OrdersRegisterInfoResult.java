package com.loopers.application.orders.result;

import com.loopers.domain.orders.OrderStatus;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersRegisterInfoResult {

    private Long ordersId;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private LocalDateTime orderDate;

    private BigDecimal totalPrice;

    private int totalCount;

    private OrdersRegisterInfoResult(Long ordersId, OrderStatus orderStatus, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        this.ordersId = ordersId;
        this.orderStatus = orderStatus;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
    }

    public static OrdersRegisterInfoResult of(Long ordersId, OrderStatus orderStatus, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        validate(ordersId, orderStatus, orderDate, totalPrice, totalCount);

        return new OrdersRegisterInfoResult(ordersId, orderStatus, orderDate, totalPrice, totalCount);
    }

    private static void validate(Long ordersId, OrderStatus orderStatus, LocalDateTime orderDate, BigDecimal totalPrice, int totalCount) {
        if (ordersId == null || ordersId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 주문 ID가 필요합니다.");
        }
        if (orderStatus == null) {
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
