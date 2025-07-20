package com.loopers.application.member;

import com.loopers.application.member.dto.MemberMyInfo;
import com.loopers.application.member.dto.MemberPointInfo;
import com.loopers.application.member.dto.MemberRegisterCommand;
import com.loopers.application.member.dto.MemberRegisterInfo;
import com.loopers.domain.member.MemberModel;
import com.loopers.domain.member.MemberRepository;
import com.loopers.interfaces.api.member.dto.request.PointChargeReqDTO;
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
    public MemberRegisterInfo register(MemberRegisterCommand command) {
        MemberModel memberModel = MemberModel.registerMember(
                command.loginId(),
                command.password(),
                command.email(),
                command.name(),
                command.birth(),
                command.gender()
        );

        MemberModel saved = memberRepository.register(memberModel).orElseThrow(()
                -> new CoreException(MemberErrorType.FAIL_REGISTER, "회원가입에 실패했습니다. 로그인 ID: " + command.loginId()));

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

    /*
     * 사용자 포인트 조회
     * */
    @Transactional(readOnly = true)
    public MemberPointInfo getMemberPoint(String memberId) {
        long id = Long.parseLong(memberId);

        Optional<MemberModel> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            return null;
        }

        MemberModel member = findMember.get();

        return MemberPointInfo.from(member.getId(), member.getPoint());
    }

    /*
     * 사용자 포인트 충전
     * */
    @Transactional
    public MemberPointInfo chargeMemberPoint(PointChargeReqDTO reqDTO) {
        long id = Long.parseLong(reqDTO.getMemberId());

        MemberModel memberModel = memberRepository.findById(id).orElseThrow(() -> {
            throw new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "유저를 찾을 수 없습니다.");
        });

        if (memberModel == null) {
            return null;
        }

        memberModel.chargePoint(reqDTO.getAmount());

        return MemberPointInfo.from(id, memberModel.getPoint());
    }
}
