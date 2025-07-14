package com.loopers.interfaces.api.member;

import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.domain.member.MemberService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemberV1Controller implements MemberV1ApiSpec {

    private final MemberService memberService;

    /**
     * 사용자 회원가입
     */
    @PostMapping
    public ApiResponse<MemberRegisterResDTO> registerMember(@RequestBody @Valid MemberRegisterReqDTO reqDTO) {
        MemberRegisterInfo memberRegisterInfo = memberService.register(reqDTO);
        MemberRegisterResDTO resDTO = MemberRegisterResDTO.from(memberRegisterInfo);

        return ApiResponse.success(resDTO);
    }
}
