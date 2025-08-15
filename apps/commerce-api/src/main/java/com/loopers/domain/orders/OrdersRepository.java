package com.loopers.domain.orders;

import java.util.List;

public interface OrdersRepository {

    Orders register(Orders orders);

    List<Orders> findAllByMemberId(Long memberId);
}
