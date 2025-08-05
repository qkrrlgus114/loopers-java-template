package com.loopers.infrastructure.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CounponMemberRepositoryImpl implements CouponMemberRepository {

    private final CouponMemberJpaRepository couponMemberJpaRepository;

    @Override
    public List<CouponMember> findAllByMemberId(Long memberId) {
        return couponMemberJpaRepository.findAllCouponByMemberIdAndIsActive(memberId);
    }

    @Override
    public CouponMember save(CouponMember couponMember) {
        return couponMemberJpaRepository.save(couponMember);
    }

    @Override
    public CouponMember findByCouponIdAndMemberId(Long couponId, Long memberId) {
        return couponMemberJpaRepository.findByCouponIdAndMemberIdIsActive(couponId, memberId)
                .orElseThrow(() -> new CoreException(CommonErrorType.BAD_REQUEST, "쿠폰을 찾을 수 없습니다. couponId: " + couponId + ", memberId: " + memberId));
    }
}
