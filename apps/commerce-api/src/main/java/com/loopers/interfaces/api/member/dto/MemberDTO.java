package com.loopers.interfaces.api.member.dto;

import com.loopers.application.member.dto.MemberRegisterInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class RegisterRequest {
        @NotNull
        private String loginId;

        @NotNull
        private String password;

        @NotNull
        @Email
        private String email;

        @NotNull
        private String name;

        @NotNull
        private String birth;

        @NotNull
        private String gender;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class RegisterResponse {
        private Long id;
        private String loginId;
        private String email;
        private String name;
        private String birth;
        private String gender;

        public static RegisterResponse from(MemberRegisterInfo info) {
            return RegisterResponse.builder()
                    .id(info.id())
                    .loginId(info.loginId())
                    .email(info.email())
                    .name(info.name())
                    .birth(info.birth())
                    .gender(info.gender())
                    .build();
        }
    }
}
