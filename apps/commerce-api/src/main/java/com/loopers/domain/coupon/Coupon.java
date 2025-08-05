package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "coupon")
public class Coupon extends BaseEntity {

    // 쿠폰 이름
    @Column(length = 20, nullable = false)
    private String name;

    // 할인 타입
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = true)
    // 정액 할인
    private Integer amount;

    @Column(nullable = true)
    // 정률 할인
    private Integer rate;

    // 최소 사용 금액
    @Column(nullable = false)
    private BigDecimal minimumPrice;

    // 만료 기간(발급하면 이 기간 이후 만료됩니다.)
    @Column(nullable = false)
    private Integer expirationDays;

    protected Coupon() {
    }

    public Coupon(String name, CouponType couponType, Integer amount, Integer rate, BigDecimal minimumPrice, Integer expirationDays) {
        this.name = name;
        this.couponType = couponType;
        this.amount = amount;
        this.rate = rate;
        this.minimumPrice = minimumPrice;
        this.expirationDays = expirationDays;
    }

    public static Coupon create(String name, CouponType couponType, Integer amount, Integer rate, BigDecimal minimumPrice, Integer expirationDays) {
        validate(name, couponType, amount, rate, minimumPrice, expirationDays);
        return new Coupon(name, couponType, amount, rate, minimumPrice, expirationDays);
    }

    private static void validate(String name, CouponType couponType, Integer amount, Integer rate, BigDecimal minimumPrice, Integer expirationDays) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("쿠폰 이름은 1자 이상 20자 이내여야 합니다.");
        }
        if (couponType != CouponType.FIXED_AMOUNT && couponType != CouponType.PERCENTAGE) {
            throw new IllegalArgumentException("할인 타입은 '정액' 또는 '정률'만 가능합니다.");
        }
        if (amount != null && amount < 0) {
            throw new IllegalArgumentException("정액 할인 금액은 0 이상이어야 합니다.");
        }
        if (rate != null && (rate < 0 || rate > 100)) {
            throw new IllegalArgumentException("정률 할인 비율은 0 이상 100 이하이어야 합니다.");
        }
        if (minimumPrice == null || minimumPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("최소 사용 금액은 0 이상이어야 합니다.");
        }
        if (expirationDays == null || expirationDays <= 0) {
            throw new IllegalArgumentException("만료 기간은 1일 이상이어야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getRate() {
        return rate;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public Integer getExpirationDays() {
        return expirationDays;
    }
}
