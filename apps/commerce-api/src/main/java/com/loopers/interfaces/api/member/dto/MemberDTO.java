package com.loopers.interfaces.api.member.dto;

import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberPointResult;
import com.loopers.application.member.result.MemberRegisterResult;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

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

        public static RegisterResponse from(MemberRegisterResult info) {
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class PointChargeRequest {

        @NotNull
        private String memberId;

        @NotNull
        private Long amount;

    }

    /*
     * 사용자의 포인트 정보 응답 DTO
     * */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MemberPointInfoResponse {

        private String memberId;

        private Long point;

        public static MemberPointInfoResponse from(MemberPointResult memberPointResult) {
            return MemberPointInfoResponse.builder()
                    .memberId(String.valueOf(memberPointResult.memberId()))
                    .point(memberPointResult.point())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class MemberInfoResponse {

        private String id;

        private String loginId;

        private String email;

        private String name;

        private String birth;

        private String gender;

        public static MemberInfoResponse from(MemberInfoResult memberInfoResult) {

            return MemberInfoResponse.builder()
                    .id(String.valueOf(memberInfoResult.id()))
                    .loginId(memberInfoResult.loginId())
                    .email(memberInfoResult.email())
                    .name(memberInfoResult.name())
                    .birth(Objects.requireNonNull(memberInfoResult.birth().toString()))
                    .gender(memberInfoResult.gender()).build();
        }
    }

}
