package com.loopers.application.member.dto;

import com.loopers.domain.member.MemberModel;

import java.time.LocalDate;

public record MemberMyInfo(
        Long id,
        String loginId,
        String email,
        String name,
        LocalDate birth,
        String gender) {

    public static MemberMyInfo from(MemberModel member) {

        return new MemberMyInfo(
                member.getId(),
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                member.getBirth(),
                member.getGender()
        );
    }
}

