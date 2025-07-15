package com.loopers.domain.member;

import com.loopers.application.member.MemberMyInfo;
import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    /*
     * 회원가입
     * */
    @Transactional
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

    /*
     * 내 정보 조회
     * */
    @Transactional(readOnly = true)
    public MemberMyInfo getMyMemberInfo(String memberId) {
        long id = Long.parseLong(memberId);

        Optional<MemberModel> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            log.error("[MemberService.getMyMemberInfo] 회원 정보 조회 실패, memberId: {}", memberId);
            return null;
        }

        return MemberMyInfo.from(findMember.get());
    }
}
