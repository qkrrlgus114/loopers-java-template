package com.loopers.application.coupon;

import com.loopers.application.couponmember.result.MyCouponListResut;
import com.loopers.application.member.MemberFacade;
import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.interfaces.api.member.dto.MemberDTO;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CouponServiceIntegrationTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private MemberFacade memberFacade;

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
}
