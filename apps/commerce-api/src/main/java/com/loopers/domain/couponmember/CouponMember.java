package com.loopers.domain.couponmember;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.coupon.CouponStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_member")
public class CouponMember extends BaseEntity {

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
    private LocalDateTime expirationAt;

    // 사용일
    @Column(nullable = true)
    private LocalDateTime usedAt;

    @Column(nullable = true)
    private Long orderItemId;

    protected CouponMember() {
    }

    public CouponMember(Long memberId, Long couponId, CouponStatus status, LocalDateTime expirationAt, Long orderItemId) {
        this.memberId = memberId;
        this.couponId = couponId;
        this.status = status;
        this.expirationAt = expirationAt;
        this.usedAt = null;
        this.orderItemId = orderItemId;
    }

    public static CouponMember create(Long memberId, Long couponId, CouponStatus status, LocalDateTime expirationAt, Long orderItemId) {
        validate(memberId, couponId, status, expirationAt, orderItemId);
        return new CouponMember(memberId, couponId, status, expirationAt, orderItemId);
    }

    private static void validate(Long memberId, Long couponId, CouponStatus status, LocalDateTime expirationAt, Long orderItemId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
        }
        if (couponId == null || couponId <= 0) {
            throw new IllegalArgumentException("유효한 쿠폰 ID가 필요합니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("쿠폰 상태는 필수입니다.");
        }
        if (expirationAt == null || expirationAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("유효한 만료일이 필요합니다.");
        }
        if (orderItemId != null && orderItemId <= 0) {
            throw new IllegalArgumentException("유효한 주문 아이템 ID가 필요합니다.");
        }
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }
}
