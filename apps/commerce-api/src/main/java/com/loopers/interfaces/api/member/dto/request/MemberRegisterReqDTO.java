package com.loopers.interfaces.api.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRegisterReqDTO {

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
