package com.loopers.application.orders.service;

import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.OrdersRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;

    /*
     * 주문 생성
     * */
    public Orders register(Long memberId, int quantity, BigDecimal totalPrice, Long couponMemberId, boolean couponUsed, String orderKey) {
        Orders orders = null;

        orders = Orders.create(memberId, quantity, totalPrice, couponMemberId, couponUsed, orderKey);
        return ordersRepository.register(orders);
    }

    /*
     * 사용자의 모든 주문 조회
     * */
    public List<Orders> findAllByMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }

        return ordersRepository.findAllByMemberId(memberId);
    }
}
