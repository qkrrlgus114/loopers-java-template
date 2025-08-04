package com.loopers.application.member;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.application.member.service.MemberService;
import com.loopers.application.point.result.PointChargeResult;
import com.loopers.application.point.result.PointInfoResult;
import com.loopers.application.point.service.PointService;
import com.loopers.application.pointhistory.service.PointHistoryService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.history.PointHistoryStatus;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final PointService pointService;
    private final PointHistoryService pointHistoryService;

    /*
     * 사용자 회원가입
     * */
    @Transactional
    public MemberRegisterResult registerMember(MemberRegisterCommand command) {
        MemberRegisterResult register = memberService.register(command);
        pointService.register(register.id());

        return register;
    }

    /*
     * 사용자 포인트 조회
     * */
    @Transactional(readOnly = true)
    public PointInfoResult getMemberPoint(Long memberId) {
        MemberInfoResult memberInfo = memberService.getMemberInfo(memberId);
        if (memberInfo == null) {
            throw new CoreException(CommonErrorType.NOT_FOUND, "회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId);
        }

        Point point = pointService.getPointByMemberId(memberInfo.id());

        return PointInfoResult.of(memberInfo, point);
    }

    /*
     * 사용자 포인트 충전
     * */
    @Transactional
    public PointChargeResult chargePoint(PointChargeCommand command) {
        Point point = pointService.getPointByMemberId(command.memberId());

        point.charge(command.amount());

        pointHistoryService.savePointHistory(command.memberId(), point.getId(), command.amount(), PointHistoryStatus.CHARGED);

        return PointChargeResult.of(
                point.getMemberId(),
                point.getAmount()
        );
    }
}
