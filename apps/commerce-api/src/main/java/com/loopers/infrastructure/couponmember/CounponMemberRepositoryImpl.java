package com.loopers.infrastructure.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
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
}
