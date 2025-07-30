package com.loopers.application.member.result;

import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.domain.member.Member;

public record MemberPointResult(
        Long memberId,
        Long point
) {
    public static MemberPointResult fromChargePoint(PointChargeCommand pointChargeCommand) {
        return new MemberPointResult(
                pointChargeCommand.memberId(),
                pointChargeCommand.amount());
    }

    public static MemberPointResult fromGetPoint(Member model) {
        return new MemberPointResult(
                model.getId(),
                model.getPoint());
    }
}
