package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberService;
import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberPointResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.MemberDTO;
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
public class MemberV1Controller {

    private final MemberService memberService;

    /*
     * 사용자 회원가입
     * */
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
    @GetMapping("/users/{memberId}")
    public ApiResponse<MemberDTO.MemberInfoResponse> getMemberInfo(
            @PathVariable @NotNull String memberId,
            @RequestHeader(name = "X-USER-ID") String headerId) {
        MemberInfoResult memberInfoResult = memberService.getMemberInfo(memberId);

        if (memberInfoResult == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberDTO.MemberInfoResponse resDTO = MemberDTO.MemberInfoResponse.from(memberInfoResult);

        return ApiResponse.success(resDTO);
    }

    /*
     * 현재 사용자 포인트 조회
     * */
    @GetMapping("/points")
    public ApiResponse<MemberDTO.MemberPointInfoResponse> getMemberPoint(
            @RequestParam String memberId,
            @RequestHeader(name = "X-USER-ID") String headerId) {
        MemberPointResult memberPointResult = memberService.getMemberPoint(memberId);

        if (memberPointResult == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberDTO.MemberPointInfoResponse resDTO = MemberDTO.MemberPointInfoResponse.from(memberPointResult);

        return ApiResponse.success(resDTO);
    }

    /*
     * 포인트 충전
     * */
    @PostMapping("/points")
    public ApiResponse<MemberDTO.MemberPointInfoResponse> chargeMemberPoint(
            @RequestHeader(name = "X-USER-ID") String headerId,
            @RequestBody MemberDTO.PointChargeRequest reqDTO) {

        PointChargeCommand pointChargeCommand = new PointChargeCommand(
                Long.valueOf(reqDTO.getMemberId()),
                reqDTO.getAmount()
        );

        MemberPointResult memberPointResult = memberService.chargeMemberPoint(pointChargeCommand);

        MemberDTO.MemberPointInfoResponse resDTO = MemberDTO.MemberPointInfoResponse.from(memberPointResult);

        return ApiResponse.success(resDTO);
    }

}
