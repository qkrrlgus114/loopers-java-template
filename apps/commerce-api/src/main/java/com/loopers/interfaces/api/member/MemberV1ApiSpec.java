package com.loopers.interfaces.api.member;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member V1 API", description = "사용자 API 버전 1입니다.")
public interface MemberV1ApiSpec {

    @Operation(
            summary = "사용자 회원가입",
            description = "사용자를 회원가입합니다."
    )
    ApiResponse<MemberRegisterResDTO> registerMember(
            @Schema(name = "회원가입 정보", description = "회원가입을 위한 정보")
            MemberRegisterReqDTO reqDTO
    );
}
