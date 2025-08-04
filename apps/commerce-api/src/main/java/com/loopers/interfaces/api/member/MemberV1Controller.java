package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberFacade;
import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.application.member.service.MemberService;
import com.loopers.application.point.result.PointChargeResult;
import com.loopers.application.point.result.PointInfoResult;
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

import java.math.BigDecimal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class MemberV1Controller {

    private final MemberService memberService;
    private final MemberFacade memberFacade;

    /*
     * 사용자 회원가입
     * */
    @PostMapping("/users")
    public ApiResponse<MemberDTO.RegisterResponse> registerMember(
            @RequestBody @Valid MemberDTO.RegisterRequest reqDTO) {
        MemberRegisterCommand command = MemberRegisterCommand.of(
                reqDTO.getLoginId(),
                reqDTO.getPassword(),
                reqDTO.getEmail(),
                reqDTO.getName(),
                reqDTO.getBirth(),
                reqDTO.getGender()
        );

        MemberRegisterResult memberRegisterResult = memberFacade.registerMember(command);

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
        MemberInfoResult memberInfoResult = memberService.getMemberInfo(Long.valueOf(memberId));

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
        PointInfoResult pointInfoResult = memberFacade.getMemberPoint(Long.valueOf(memberId));

        if (pointInfoResult == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberDTO.MemberPointInfoResponse resDTO = MemberDTO.MemberPointInfoResponse.from(pointInfoResult);

        return ApiResponse.success(resDTO);
    }

    /*
     * 포인트 충전
     * */
    @PostMapping("/points")
    public ApiResponse<PointChargeResult> chargeMemberPoint(
            @RequestHeader(name = "X-USER-ID") String headerId,
            @RequestBody MemberDTO.PointChargeRequest reqDTO) {

        PointChargeCommand pointChargeCommand = new PointChargeCommand(
                Long.valueOf(reqDTO.getMemberId()),
                BigDecimal.valueOf(reqDTO.getAmount())
        );

        PointChargeResult pointChargeResult = memberFacade.chargePoint(pointChargeCommand);

        MemberDTO.MemberPointInfoResponse.builder()
                .memberId(String.valueOf(pointChargeResult.memberId()))
                .point(pointChargeResult.amount().longValue())
                .build();

        return ApiResponse.success(pointChargeResult);
    }

}
