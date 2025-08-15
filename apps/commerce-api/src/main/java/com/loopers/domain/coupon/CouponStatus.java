package com.loopers.domain.coupon;

public enum CouponStatus {
    ACTIVE("활성화"),
    EXPIRED("만료됨"),
    USED("사용됨");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
