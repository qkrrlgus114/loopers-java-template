package com.loopers.application.member.dto;

import com.loopers.domain.member.MemberModel;

public record MemberRegisterInfo(
        Long id,
        String loginId,
        String email,
        String name,
        String birth,
        String gender) {

    public static MemberRegisterInfo from(MemberModel member) {
        String birthStr = member.getBirth() != null ? member.getBirth().toString() : null;

        return new MemberRegisterInfo(
                member.getId(),
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                birthStr,
                member.getGender()
        );
    }
}
