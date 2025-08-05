package com.loopers.domain.coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findAllByIds(List<Long> couponIds);

    void save(Coupon coupon);
}
