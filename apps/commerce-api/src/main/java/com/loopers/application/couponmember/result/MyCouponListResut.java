package com.loopers.application.couponmember.result;

import com.loopers.domain.coupon.CouponType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MyCouponListResut(
        Long couponId,
        String name,
        CouponType couponType,
        BigDecimal amount,
        Integer rate,
        BigDecimal minimumPrice,
        LocalDateTime expirationAt
) {
    public static MyCouponListResut of(Long couponId, String name, CouponType couponType, BigDecimal amount, Integer rate, BigDecimal minimumPrice, LocalDateTime expirationAt) {
        return new MyCouponListResut(couponId, name, couponType, amount, rate, minimumPrice, expirationAt);
    }
}
