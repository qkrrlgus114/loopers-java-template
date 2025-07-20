package com.loopers.application.member.dto;

public record MemberRegisterCommand(
        String loginId,
        String password,
        String email,
        String name,
        String birth,
        String gender
) {
}
