package com.loopers.application.member;

public record MemberPointInfo(
        String memberId,
        Long point
) {
    public static MemberPointInfo from(Long memberId, Long point) {
        return new MemberPointInfo(
                String.valueOf(memberId),
                point);
    }
}
