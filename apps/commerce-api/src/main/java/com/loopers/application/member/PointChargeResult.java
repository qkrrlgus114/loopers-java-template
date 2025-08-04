package com.loopers.application.member;

import java.math.BigDecimal;

public record PointChargeResult(
        Long memberId,
        BigDecimal amount
) {
    public static PointChargeResult of(Long memberId, BigDecimal amount) {
        return new PointChargeResult(memberId, amount);
    }
}
