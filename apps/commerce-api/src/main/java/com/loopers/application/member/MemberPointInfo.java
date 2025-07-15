package com.loopers.application.member;

public record MemberPointInfo(
        String memberId,
        String point
) {
    public static MemberPointInfo from(Long memberId, Long point) {
        return new MemberPointInfo(
                String.valueOf(memberId),
                String.valueOf(point));
    }
}
