package com.loopers.domain.coupon;

public enum CouponType {
    FIXED_AMOUNT("F"), // 정액 할인
    PERCENTAGE("R"); // 정률 할인

    private final String code;

    CouponType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
