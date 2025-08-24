package com.loopers.infrastructure.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponMemberJpaRepository extends JpaRepository<CouponMember, Long> {

    @Query("SELECT cm FROM CouponMember cm WHERE cm.memberId = :memberId AND cm.status = 'ACTIVE'")
    List<CouponMember> findAllCouponByMemberIdAndIsActive(Long memberId);

    @Query("SELECT cm FROM CouponMember cm WHERE cm.couponId = :couponId AND cm.memberId = :memberId AND cm.status = 'ACTIVE'")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponMember> findByCouponIdAndMemberIdIsActive(Long couponId, Long memberId);

    @Query("SELECT cm FROM CouponMember cm WHERE cm.couponId = :couponId AND cm.memberId = :memberId AND cm.status = 'USED'")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponMember> findByCouponIdAndMemberIdIsUsedWithLock(Long couponId, Long memberId);
}
