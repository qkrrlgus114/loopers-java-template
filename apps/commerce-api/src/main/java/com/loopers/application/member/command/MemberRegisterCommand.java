package com.loopers.application.member.command;

public record MemberRegisterCommand(
        String loginId,
        String password,
        String email,
        String name,
        String birth,
        String gender
) {
}
