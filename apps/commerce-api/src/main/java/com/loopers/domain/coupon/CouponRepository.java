package com.loopers.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findAllByIds(List<Long> couponIds);

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);
}
