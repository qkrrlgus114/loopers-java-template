package com.loopers.application.member.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record MemberRegisterCommand(
        String loginId,
        String password,
        String email,
        String name,
        LocalDate birth,
        String gender
) {

    public static MemberRegisterCommand of(
            String loginId, String password, String email,
            String name, String birthStr, String gender) {

        LocalDate birth;
        try {
            birth = LocalDate.parse(birthStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다. (yyyy-MM-dd)", e);
        }

        return new MemberRegisterCommand(loginId, password, email, name, birth, gender);
    }
}
