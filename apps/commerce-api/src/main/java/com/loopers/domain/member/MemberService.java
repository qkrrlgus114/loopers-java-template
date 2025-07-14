package com.loopers.domain.member;

import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    public MemberRegisterInfo register(MemberRegisterReqDTO reqDTO) {
        MemberModel memberModel = new MemberModel(
                reqDTO.getLoginId(),
                reqDTO.getPassword(),
                reqDTO.getEmail(),
                reqDTO.getName(),
                reqDTO.getBirth(),
                reqDTO.getGender()
        );

        MemberModel saved = memberRepository.register(memberModel).orElseThrow(() -> {
            return new CoreException(MemberErrorType.FAIL_REGISTER, "회원가입에 실패했습니다. 로그인 ID: " + reqDTO.getLoginId());
        });

        return MemberRegisterInfo.from(saved);
    }

}
