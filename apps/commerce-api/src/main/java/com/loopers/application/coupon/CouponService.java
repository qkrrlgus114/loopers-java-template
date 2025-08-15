package com.loopers.application.coupon;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getCouponByIds(List<Long> couponIds) {
        return couponRepository.findAllByIds(couponIds);
    }

    public Coupon getCouponId(Long couponId) {
        return couponRepository.findById(couponId);
    }
}
