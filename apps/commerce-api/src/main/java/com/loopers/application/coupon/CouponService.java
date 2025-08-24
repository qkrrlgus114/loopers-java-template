package com.loopers.application.coupon;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
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
        return couponRepository.findById(couponId).orElseThrow(() ->
                new CoreException(CommonErrorType.NOT_FOUND, "쿠폰을 찾을 수 없습니다. couponId: " + couponId));
    }
}
