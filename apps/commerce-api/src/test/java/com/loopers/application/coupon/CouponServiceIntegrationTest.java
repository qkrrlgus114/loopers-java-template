package com.loopers.application.coupon;

import com.loopers.application.couponmember.result.MyCouponListResut;
import com.loopers.application.member.MemberFacade;
import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponStatus;
import com.loopers.domain.coupon.CouponType;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import com.loopers.interfaces.api.member.dto.MemberDTO;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private MemberFacade memberFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponMemberRepository couponMemberRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private MemberRegisterCommand memberRegisterCommand;

    @BeforeEach
    void setUp() {
        MemberDTO.RegisterRequest setUpMemberReqDTO = MemberDTO.RegisterRequest.builder()
                .loginId("test")
                .password("test")
                .email("test@naver.com")
                .name("박기현")
                .gender("M")
                .birth("1997-12-04").build();

        memberRegisterCommand = MemberRegisterCommand.of(
                setUpMemberReqDTO.getLoginId(),
                setUpMemberReqDTO.getPassword(),
                setUpMemberReqDTO.getEmail(),
                setUpMemberReqDTO.getName(),
                setUpMemberReqDTO.getBirth(),
                setUpMemberReqDTO.getGender()
        );
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("유저가 존재하지 않는 경우 에러를 반환한다.")
    @Test
    void fail_myCouponList_notExistMember() {
        // Given
        Long memberId = 999L;

        // When & Then
        assertThrows(CoreException.class, () -> couponFacade.getMyCouponList(memberId));
    }

    @DisplayName("내가 가진 쿠폰 목록을 정상적으로 조회한다.(쿠폰 존재하지 않음)")
    @Test
    void success_myCouponList_noCoupon() {
        // Given
        MemberRegisterResult memberRegisterResult = memberFacade.registerMember(memberRegisterCommand);

        // then
        List<MyCouponListResut> myCouponList = couponFacade.getMyCouponList(memberRegisterResult.id());

        // When
        assertNotNull(myCouponList);
        assertEquals(myCouponList.size(), 0);
    }

    @DisplayName("내가 가진 쿠폰 목록을 정상적으로 조회한다.(정률 쿠폰 1개 존재함)")
    @Test
    void success_myCouponList_oneCoupon() {
        // Given
        MemberRegisterResult memberRegisterResult = memberFacade.registerMember(memberRegisterCommand);
        Coupon coupon = Coupon.create(
                "10% 할인 쿠폰",
                CouponType.PERCENTAGE,
                null,
                10,
                BigDecimal.valueOf(1000L),
                30
        );
        Coupon savedCoupon = couponRepository.save(coupon);

        CouponMember couponMember = CouponMember.create(
                memberRegisterResult.id(),
                savedCoupon.getId(),
                CouponStatus.ACTIVE,
                LocalDateTime.now().plusDays(5),
                null
        );
        CouponMember savedCouponMember = couponMemberRepository.save(couponMember);


        // When
        List<MyCouponListResut> myCouponList = couponFacade.getMyCouponList(memberRegisterResult.id());

        // Then
        assertAll(
                () -> assertNotNull(myCouponList),
                () -> assertEquals(myCouponList.size(), 1),
                () -> assertEquals(myCouponList.get(0).couponType(), CouponType.PERCENTAGE),
                () -> assertEquals(myCouponList.get(0).amount(), null),
                () -> assertEquals(myCouponList.get(0).rate(), 10),
                () -> assertEquals(0, BigDecimal.valueOf(1000).compareTo(myCouponList.get(0).minimumPrice())),
                () -> assertNotNull(myCouponList.get(0).expirationAt())
        );
    }

    @DisplayName("내가 가진 쿠폰 목록을 정상적으로 조회한다.(정액 쿠폰 1개 존재함)")
    @Test
    void success_myCouponList_oneFixedAmountCoupon() {
        // Given
        MemberRegisterResult memberRegisterResult = memberFacade.registerMember(memberRegisterCommand);
        Coupon coupon = Coupon.create(
                "5000원 할인 쿠폰",
                CouponType.FIXED_AMOUNT,
                BigDecimal.valueOf(5000L),
                null,
                BigDecimal.valueOf(10000L),
                30
        );
        Coupon savedCoupon = couponRepository.save(coupon);

        CouponMember couponMember = CouponMember.create(
                memberRegisterResult.id(),
                savedCoupon.getId(),
                CouponStatus.ACTIVE,
                LocalDateTime.now().plusDays(5),
                null
        );
        couponMemberRepository.save(couponMember);

        // When
        List<MyCouponListResut> myCouponList = couponFacade.getMyCouponList(memberRegisterResult.id());

        // Then
        assertAll(
                () -> assertNotNull(myCouponList),
                () -> assertEquals(myCouponList.size(), 1),
                () -> assertEquals(myCouponList.get(0).couponType(), CouponType.FIXED_AMOUNT),
                () -> assertEquals(0, myCouponList.get(0).amount().compareTo(BigDecimal.valueOf(5000L))),
                () -> assertEquals(myCouponList.get(0).rate(), null),
                () -> assertEquals(0, BigDecimal.valueOf(10000).compareTo(myCouponList.get(0).minimumPrice())),
                () -> assertNotNull(myCouponList.get(0).expirationAt())
        );
    }
}
