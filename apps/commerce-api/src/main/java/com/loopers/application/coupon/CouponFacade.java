package com.loopers.application.coupon;

import com.loopers.application.couponmember.CouponMemberService;
import com.loopers.application.couponmember.result.MyCouponListResut;
import com.loopers.application.member.service.MemberService;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponMemberService couponMemberService;
    private final CouponService couponService;
    private final MemberService memberService;

    /*
     * 내가 가진 쿠폰 목록 조회
     *
     * 1. 쿠폰 멤버 리스트 조회
     * 2. 쿠폰 조회
     * 3. 조합
     * */
    public List<MyCouponListResut> getMyCouponList(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        
        // 1. 쿠폰 멤버 리스트 조회
        List<CouponMember> couponMemberList = couponMemberService.getMyCouponList(memberId);
        if (couponMemberList == null || couponMemberList.isEmpty()) {
            return List.of();
        }
        List<Long> couponIds = couponMemberList.stream()
                .map(CouponMember::getCouponId).toList();

        // 2. 쿠폰 조회
        List<Coupon> couponList = couponService.getCouponByIds(couponIds);
        if (couponList == null || couponList.isEmpty()) {
            return List.of();
        }
        Map<Long, Coupon> couponMap = couponList.stream()
                .collect(Collectors.toMap(Coupon::getId, coupon -> coupon));

        // 3. 조합
        List<MyCouponListResut> result = new ArrayList<>();
        for (CouponMember couponMember : couponMemberList) {
            Coupon coupon = couponMap.get(couponMember.getCouponId());
            MyCouponListResut myCouponListResut = MyCouponListResut.of(
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getCouponType(),
                    coupon.getAmount(),
                    coupon.getRate(),
                    coupon.getMinimumPrice(),
                    couponMember.getExpirationAt()
            );

            result.add(myCouponListResut);
        }

        return result;
    }

}
