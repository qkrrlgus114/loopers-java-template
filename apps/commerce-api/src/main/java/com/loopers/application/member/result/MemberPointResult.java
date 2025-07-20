package com.loopers.application.member.result;

public record MemberPointResult(
        Long memberId,
        Long point
) {
    public static MemberPointResult from(Long memberId, Long point) {
        return new MemberPointResult(
                memberId,
                point);
    }
}
