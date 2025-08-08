package com.loopers.infrastructure.orders;

import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.OrdersRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepository {

    private final OrdersJpaRepository ordersJpaRepository;

    public Orders register(Orders orders) {
        return ordersJpaRepository.save(orders);
    }

    @Override
    public List<Orders> findAllByMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        return ordersJpaRepository.findAllByMemberId(memberId);
    }
}
