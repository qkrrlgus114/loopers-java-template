package com.loopers.application.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterReqDTO {

    private String loginId;

    private String password;

    private String email;

    private String name;

    private String birth;

}
