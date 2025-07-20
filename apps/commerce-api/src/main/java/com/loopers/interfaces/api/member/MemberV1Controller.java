package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberService;
import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberPointResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.MemberDTO;
import com.loopers.interfaces.api.member.dto.request.PointChargeReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberInfoResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberPointResDTO;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class MemberV1Controller implements MemberV1ApiSpec {

    private final MemberService memberService;

    /*
     * 사용자 회원가입
     * */
    @Override
    @PostMapping("/users")
    public ApiResponse<MemberDTO.RegisterResponse> registerMember(
            @RequestBody @Valid MemberDTO.RegisterRequest reqDTO) {
        MemberRegisterCommand command = new MemberRegisterCommand(
                reqDTO.getLoginId(),
                reqDTO.getPassword(),
                reqDTO.getEmail(),
                reqDTO.getName(),
                reqDTO.getBirth(),
                reqDTO.getGender()
        );
        MemberRegisterResult memberRegisterResult = memberService.register(command);

        MemberDTO.RegisterResponse resDTO = MemberDTO.RegisterResponse.from(memberRegisterResult);

        return ApiResponse.success(resDTO);
    }

    /*
     * 사용자 정보 조회
     * */
    @Override
    @GetMapping("/users/{memberId}")
    public ApiResponse<MemberInfoResDTO> getMemberInfo(
            @PathVariable @NotNull String memberId,
            @RequestHeader(name = "X-USER-ID") String headerId) {
        MemberInfoResult memberInfoResult = memberService.getMemberInfo(memberId);

        if (memberInfoResult == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberInfoResDTO resDTO = MemberInfoResDTO.from(memberInfoResult);

        return ApiResponse.success(resDTO);
    }

    /*
     * 사용자 포인트 조회
     * */
    @Override
    @GetMapping("/points")
    public ApiResponse<MemberPointResDTO> getMemberPoint(
            @RequestParam String memberId,
            @RequestHeader(name = "X-USER-ID") String headerId) {
        MemberPointResult memberPoint = memberService.getMemberPoint(memberId);

        if (memberPoint == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberPointResDTO resDTO = MemberPointResDTO.from(memberPoint.memberId(), memberPoint.point());

        return ApiResponse.success(resDTO);
    }

    @Override
    @PostMapping("/points")
    public ApiResponse<MemberPointResDTO> chargeMemberPoint(
            @RequestHeader(name = "X-USER-ID") String headerId,
            @RequestBody PointChargeReqDTO reqDTO) {

        MemberPointResult memberPointResult = memberService.chargeMemberPoint(reqDTO);
        MemberPointResDTO resDTO = MemberPointResDTO.from(memberPointResult.memberId(), Long.valueOf(memberPointResult.point()));

        return ApiResponse.success(resDTO);
    }

}
