package com.loopers.interfaces.api.member.dto.response;

import com.loopers.application.member.MemberRegisterInfo;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterResDTO {

    private String loginId;

    private String email;

    private String name;

    private String birth;

    public static MemberRegisterResDTO from(MemberRegisterInfo info) {
        return MemberRegisterResDTO.builder()
                .loginId(info.loginId())
                .email(info.email())
                .name(info.name())
                .birth(info.birth())
                .build();
    }
}
