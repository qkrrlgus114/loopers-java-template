package com.loopers.domain.couponmember;

import java.util.List;

public interface CouponMemberRepository {

    List<CouponMember> findAllByMemberId(Long memberId);

    void save(CouponMember couponMember);

}
