package com.loopers.interfaces.api.member.dto.response;

import com.loopers.application.member.dto.MemberMyInfo;
import lombok.*;

import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberInfoResDTO {

    private Long id;

    private String loginId;

    private String email;

    private String name;

    private String birth;

    private String gender;

    public static MemberInfoResDTO from(MemberMyInfo memberMyInfo) {

        return MemberInfoResDTO.builder()
                .id(memberMyInfo.id())
                .loginId(memberMyInfo.loginId())
                .email(memberMyInfo.email())
                .name(memberMyInfo.name())
                .birth(Objects.requireNonNull(memberMyInfo.birth().toString()))
                .gender(memberMyInfo.gender()).build();
    }
}
