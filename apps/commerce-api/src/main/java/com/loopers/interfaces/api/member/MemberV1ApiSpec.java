package com.loopers.interfaces.api.member;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.request.PointChargeReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberInfoResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberPointResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Member V1 API", description = "사용자 API 버전 1입니다.")
public interface MemberV1ApiSpec {

    @Operation(
            summary = "사용자 회원가입",
            description = "사용자를 회원가입합니다."
    )
    ApiResponse<MemberRegisterResDTO> registerMember(
            @Schema(name = "회원가입 정보", description = "회원가입을 위한 정보")
            @Valid @RequestBody MemberRegisterReqDTO reqDTO
    );

    @Operation(
            summary = "사용자 정보 조회",
            description = "로그인한 사용자의 정보를 조회합니다."
    )
    ApiResponse<MemberInfoResDTO> getMyMemberInfo(
            @Schema(name = "회원 ID", description = "조회할 회원의 ID")
            @NotNull @RequestParam String memberId
    );

    @Operation(
            summary = "사용자 포인트 조회",
            description = "대상 사용자의 포인트를 조회합니다."
    )
    ApiResponse<MemberPointResDTO> getMemberPoint(
            @Schema(name = "회원 ID", description = "조회할 회원의 ID")
            @NotNull @RequestParam String memberId,
            @Schema(name = "유저 헤더", description = "인증 유저 헤더")
            @RequestHeader(name = "X-USER-ID") String headerId
    );

    @Operation(
            summary = "사용자 포인트 충천",
            description = "대상 사용자의 포인트를 충전합니다. 이후 현재 포인트를 반환합니다."
    )
    ApiResponse<MemberPointResDTO> chargeMemberPoint(
            @Schema(name = "유저 헤더", description = "인증 유저 헤더")
            @RequestHeader(name = "X-USER-ID") String headerId,
            @Schema(name = "충전 정보", description = "포인트 충전을 위한 정보")
            @Valid @RequestBody PointChargeReqDTO reqDTO
    );
}
