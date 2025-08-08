package com.loopers.domain.coupon;

import java.util.List;

public interface CouponRepository {
    List<Coupon> findAllByIds(List<Long> couponIds);

    Coupon save(Coupon coupon);

    Coupon findById(Long couponId);
}
