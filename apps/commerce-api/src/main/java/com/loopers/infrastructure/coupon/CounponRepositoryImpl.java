package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CounponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public List<Coupon> findAllByIds(List<Long> couponIds) {
        return couponJpaRepository.findAllById(couponIds);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public Coupon findById(Long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CoreException(CommonErrorType.BAD_REQUEST, "쿠폰을 찾을 수 없습니다. 쿠폰 ID: " + couponId));
    }
}
