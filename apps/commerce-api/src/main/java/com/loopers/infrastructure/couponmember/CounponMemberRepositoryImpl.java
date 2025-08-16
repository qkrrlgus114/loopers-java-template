package com.loopers.infrastructure.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<CouponMember> findByCouponIdAndMemberId(Long couponId, Long memberId) {
        return couponMemberJpaRepository.findByCouponIdAndMemberIdIsActive(couponId, memberId);
    }
}
