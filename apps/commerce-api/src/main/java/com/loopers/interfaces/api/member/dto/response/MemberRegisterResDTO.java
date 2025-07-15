package com.loopers.interfaces.api.member.dto.response;

import com.loopers.application.member.MemberRegisterInfo;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberRegisterResDTO {

    private Long id;

    private String loginId;

    private String email;

    private String name;

    private String birth;

    private String gender;

    public static MemberRegisterResDTO from(MemberRegisterInfo info) {
        return MemberRegisterResDTO.builder()
                .id(info.id())
                .loginId(info.loginId())
                .email(info.email())
                .name(info.name())
                .birth(info.birth())
                .gender(info.gender())
                .build();
    }
}
