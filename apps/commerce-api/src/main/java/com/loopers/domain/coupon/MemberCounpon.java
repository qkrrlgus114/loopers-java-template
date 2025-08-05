package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "member_coupon")
public class MemberCounpon extends BaseEntity {

    // 회원 ID
    @Column(nullable = false)
    private Long memberId;

    // 쿠폰 ID
    @Column(nullable = false)
    private Long couponId;

    // 쿠폰 상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    // 만료일
    @Column(nullable = false)
    private ZonedDateTime expirationAt;

    // 사용일
    @Column(nullable = true)
    private ZonedDateTime usedAt;

    @Column(nullable = true)
    private Long orderItemId;

    protected MemberCounpon() {
    }

    public MemberCounpon(Long memberId, Long couponId, CouponStatus status, ZonedDateTime expirationAt, Long orderItemId) {
        this.memberId = memberId;
        this.couponId = couponId;
        this.status = status;
        this.expirationAt = expirationAt;
        this.usedAt = null;
        this.orderItemId = orderItemId;
    }

    public static MemberCounpon create(Long memberId, Long couponId, CouponStatus status, ZonedDateTime expirationAt, Long orderItemId) {
        validate(memberId, couponId, status, expirationAt, orderItemId);
        return new MemberCounpon(memberId, couponId, status, expirationAt, orderItemId);
    }

    private static void validate(Long memberId, Long couponId, CouponStatus status, ZonedDateTime expirationAt, Long orderItemId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
        }
        if (couponId == null || couponId <= 0) {
            throw new IllegalArgumentException("유효한 쿠폰 ID가 필요합니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("쿠폰 상태는 필수입니다.");
        }
        if (expirationAt == null || expirationAt.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("유효한 만료일이 필요합니다.");
        }
        if (orderItemId != null && orderItemId <= 0) {
            throw new IllegalArgumentException("유효한 주문 아이템 ID가 필요합니다.");
        }
    }


}
