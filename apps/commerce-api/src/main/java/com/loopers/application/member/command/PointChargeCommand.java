package com.loopers.application.member.command;

public record PointChargeCommand(
        Long memberId,
        Long amount
) {
}
