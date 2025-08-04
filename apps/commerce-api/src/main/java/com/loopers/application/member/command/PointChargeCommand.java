package com.loopers.application.member.command;

import java.math.BigDecimal;

public record PointChargeCommand(
        Long memberId,
        BigDecimal amount
) {
}
