package com.loopers.application.member.result;

import com.loopers.domain.member.Member;

public record MemberRegisterResult(
        Long id,
        String loginId,
        String email,
        String name,
        String birth,
        String gender) {

    public static MemberRegisterResult from(Member member) {
        String birthStr = member.getBirth() != null ? member.getBirth().toString() : null;

        return new MemberRegisterResult(
                member.getId(),
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                birthStr,
                member.getGender()
        );
    }
}
