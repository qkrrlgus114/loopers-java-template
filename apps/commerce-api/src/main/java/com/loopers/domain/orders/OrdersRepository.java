package com.loopers.domain.orders;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository {

    Orders register(Orders orders);

    List<Orders> findAllByMemberId(Long memberId);

    Optional<Orders> findById(Long ordersId);

    Optional<Orders> findByOrderKeyWithLock(String orderKey);
}
