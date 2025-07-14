package com.loopers.interfaces.api.member.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRegisterReqDTO {

    private String loginId;

    private String password;

    private String email;

    private String name;

    private String birth;

}
