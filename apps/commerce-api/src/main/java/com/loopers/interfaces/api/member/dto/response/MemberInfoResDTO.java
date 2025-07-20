package com.loopers.interfaces.api.member.dto.response;

import com.loopers.application.member.result.MemberInfoResult;
import lombok.*;

import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberInfoResDTO {

    private String id;

    private String loginId;

    private String email;

    private String name;

    private String birth;

    private String gender;

    public static MemberInfoResDTO from(MemberInfoResult memberInfoResult) {

        return MemberInfoResDTO.builder()
                .id(String.valueOf(memberInfoResult.id()))
                .loginId(memberInfoResult.loginId())
                .email(memberInfoResult.email())
                .name(memberInfoResult.name())
                .birth(Objects.requireNonNull(memberInfoResult.birth().toString()))
                .gender(memberInfoResult.gender()).build();
    }
}
