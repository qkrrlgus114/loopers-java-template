package com.loopers.application.member;

import com.loopers.domain.member.MemberModel;

public record MemberMyInfo(
        Long id,
        String loginId,
        String email,
        String name,
        String birth,
        String gender) {

    public static MemberMyInfo from(MemberModel member) {
        String birthStr = member.getBirth() != null ? member.getBirth().toString() : null;

        return new MemberMyInfo(
                member.getId(),
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                birthStr,
                member.getGender()
        );
    }
}

