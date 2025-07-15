package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberMyInfo;
import com.loopers.application.member.MemberPointInfo;
import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.domain.member.MemberService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberInfoResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberPointResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
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
    public ApiResponse<MemberRegisterResDTO> registerMember(MemberRegisterReqDTO reqDTO) {
        MemberRegisterInfo memberRegisterInfo = memberService.register(reqDTO);
        MemberRegisterResDTO resDTO = MemberRegisterResDTO.from(memberRegisterInfo);

        return ApiResponse.success(resDTO);
    }

    /*
     * 사용자 정보 조회
     * */
    @Override
    @GetMapping("/users/me")
    public ApiResponse<MemberInfoResDTO> getMyMemberInfo(
            @RequestParam String memberId) {
        MemberMyInfo myMemberInfo = memberService.getMyMemberInfo(memberId);

        if (myMemberInfo == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberInfoResDTO resDTO = MemberInfoResDTO.from(myMemberInfo);

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
        MemberPointInfo memberPoint = memberService.getMemberPoint(memberId);

        if (memberPoint == null) {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        MemberPointResDTO resDTO = MemberPointResDTO.from(memberPoint.memberId(), memberPoint.point());

        return ApiResponse.success(resDTO);
    }

}
