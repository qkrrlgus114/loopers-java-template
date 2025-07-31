package com.loopers.application.member.result;

import com.loopers.domain.member.Member;

import java.time.LocalDate;

public record MemberInfoResult(
        Long id,
        String loginId,
        String email,
        String name,
        LocalDate birth,
        String gender) {

    public static MemberInfoResult from(Member member) {

        return new MemberInfoResult(
                member.getId(),
                member.getLoginId(),
                member.getEmail(),
                member.getName(),
                member.getBirth(),
                member.getGender()
        );
    }
}

