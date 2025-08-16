package com.loopers.application.couponmember;

import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponMemberService {

    private final CouponMemberRepository couponMemberRepository;

    /*
     * 내가 가진 쿠폰 목록 조회
     * */
    public List<CouponMember> getMyCouponList(Long memberId) {
        return couponMemberRepository.findAllByMemberId(memberId);
    }

    /*
     * 쿠폰 조회
     * */
    public CouponMember getCouponMemberById(Long memberId, Long couponId) {
        return couponMemberRepository.findByCouponIdAndMemberId(couponId, memberId).
                orElseThrow(() -> new CoreException(CommonErrorType.BAD_REQUEST, "쿠폰을 찾을 수 없습니다. couponId: " + couponId + ", memberId: " + memberId));
    }

}
