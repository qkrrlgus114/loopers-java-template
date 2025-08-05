package com.loopers.infrastructure.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponMemberJpaRepository extends JpaRepository<CouponMember, Long> {

    @Query("SELECT cm FROM CouponMember cm WHERE cm.memberId = :memberId AND cm.status = 'ACTIVE'")
    List<CouponMember> findAllCouponByMemberIdAndIsActive(Long memberId);

}
