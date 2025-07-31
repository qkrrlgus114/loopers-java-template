package com.loopers.application.member.service;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberPointResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
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
    public MemberRegisterResult register(MemberRegisterCommand command) {
        Member member = Member.registerMember(
                command.loginId(),
                command.password(),
                command.email(),
                command.name(),
                command.birth(),
                command.gender()
        );

        Member saved = memberRepository.register(member).orElseThrow(()
                -> new CoreException(MemberErrorType.FAIL_REGISTER, "회원가입에 실패했습니다. 로그인 ID: " + command.loginId()));

        return MemberRegisterResult.from(saved);
    }

    /*
     * 내 정보 조회
     * */
    @Transactional(readOnly = true)
    public MemberInfoResult getMemberInfo(String memberId) {
        long id = Long.parseLong(memberId);

        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            log.error("[MemberService.getMyMemberInfo] 회원 정보 조회 실패, memberId: {}", memberId);
            return null;
        }

        return MemberInfoResult.from(findMember.get());
    }

    /*
     * 사용자 포인트 조회
     * */
    @Transactional(readOnly = true)
    public MemberPointResult getMemberPoint(String memberId) {
        long id = Long.parseLong(memberId);

        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            return null;
        }

        Member member = findMember.get();

        return MemberPointResult.fromGetPoint(member);
    }

    /*
     * 사용자 포인트 충전
     * */
    @Transactional
    public MemberPointResult chargeMemberPoint(PointChargeCommand pointChargeCommand) {
        Member member = memberRepository.findById(pointChargeCommand.memberId()).orElseThrow(
                () -> new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "유저를 찾을 수 없습니다."));

        member.chargePoint(pointChargeCommand.amount());

        return MemberPointResult.fromChargePoint(pointChargeCommand);
    }

    /*
     * 사용자 조회
     * */
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(MemberErrorType.NOT_FOUND_MEMBER, "유저를 찾을 수 없습니다. memberId: " + memberId));
    }
}
