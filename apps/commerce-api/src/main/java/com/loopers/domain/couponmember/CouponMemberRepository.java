package com.loopers.domain.couponmember;

import java.util.List;
import java.util.Optional;

public interface CouponMemberRepository {

    List<CouponMember> findAllByMemberId(Long memberId);

    CouponMember save(CouponMember couponMember);

    Optional<CouponMember> findByCouponIdAndMemberId(Long couponId, Long memberId);
}
