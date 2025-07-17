package com.loopers.domain.member;

import com.loopers.application.member.MemberMyInfo;
import com.loopers.application.member.MemberPointInfo;
import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.request.PointChargeReqDTO;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private MemberRegisterReqDTO setUpMemberReqDTO;

    @BeforeEach
    void setUp() {
        setUpMemberReqDTO = MemberRegisterReqDTO.builder()
                .loginId("test")
                .password("test")
                .email("test@naver.com")
                .name("박기현")
                .gender("M")
                .birth("1997-12-04").build();
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("회원가입을 진행할 때, ")
    class Register {

        @DisplayName("유효한 회원 정보를 주면, 회원가입에 성공한다.")
        @Test
        void success_whenRegisterCollectDate() {
            // when
            MemberRegisterInfo saved = memberService.register(setUpMemberReqDTO);

            // then
            assertEquals(saved.loginId(), setUpMemberReqDTO.getLoginId());
            assertEquals(saved.email(), setUpMemberReqDTO.getEmail());
            assertEquals(saved.name(), setUpMemberReqDTO.getName());
            assertEquals(saved.birth(), setUpMemberReqDTO.getBirth());
        }

        @DisplayName("이미 가입된 아이디로 가입하면, 회원가입을 실패한다.")
        @Test
        void fail_conflictLoginIdRegister() {
            // given
            MemberRegisterReqDTO memberRegisterReqDTO = MemberRegisterReqDTO.builder()
                    .loginId("test")
                    .password("1234")
                    .email("1234@naver.com")
                    .name("박기현")
                    .gender("M")
                    .birth("1997-12-04").build();
            memberService.register(setUpMemberReqDTO);

            assertThrows(CoreException.class, () -> {
                memberService.register(memberRegisterReqDTO);
            });
        }

    }

    @Nested
    @DisplayName("해당 ID의 회원이 ")
    class GetMemberInfo {

        @DisplayName("존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnMemberInfo_whenMemberExists() {
            MemberRegisterInfo saved = memberService.register(setUpMemberReqDTO);

            String memberId = String.valueOf(saved.id());
            // when
            MemberMyInfo myMemberInfo = memberService.getMyMemberInfo(memberId);

            // then
            assertAll(
                    () -> assertThat(myMemberInfo).isNotNull(),
                    () -> assertThat(myMemberInfo.loginId()).isEqualTo(setUpMemberReqDTO.getLoginId()),
                    () -> assertThat(myMemberInfo.email()).isEqualTo(setUpMemberReqDTO.getEmail()),
                    () -> assertThat(myMemberInfo.name()).isEqualTo(setUpMemberReqDTO.getName()),
                    () -> assertThat(myMemberInfo.birth()).isEqualTo(setUpMemberReqDTO.getBirth()),
                    () -> assertThat(myMemberInfo.gender()).isEqualTo(setUpMemberReqDTO.getGender())
            );
        }

        @DisplayName("존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenMemberDoesNotExist() {
            // given
            String memberId = "9999";

            // when
            MemberMyInfo myMemberInfo = memberService.getMyMemberInfo(memberId);

            // then
            assertNull(myMemberInfo);
        }
    }

    @Nested
    @DisplayName("해당 ID의 회원이 ")
    class GetMemberPoint {
        /*
         * - [ ]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
         * - [ ]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
         * */

        @DisplayName("존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnMemberPoint_whenMemberExists() {
            // given
            MemberRegisterInfo saved = memberService.register(setUpMemberReqDTO);

            // when
            MemberPointInfo memberPointInfo = memberService.getMemberPoint(String.valueOf(saved.id()));

            // then
            assertAll(
                    () -> assertThat(memberPointInfo).isNotNull(),
                    () -> assertThat(memberPointInfo.point()).isEqualTo(0)
            );
        }

        @DisplayName("존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenMemberDoesNotExist() {
            // given
            String memberId = "9999";

            // when
            MemberPointInfo memberPointInfo = memberService.getMemberPoint(memberId);

            // then
            assertNull(memberPointInfo);
        }
    }

    @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
    @Test
    void fail_whenChargePointWithNonExistentUserId() {
        // given
        String memberId = "9999";
        PointChargeReqDTO reqDTO = PointChargeReqDTO.builder()
                .memberId(memberId)
                .amount(1000L)
                .build();


        // when & then
        assertThrows(CoreException.class, () -> {
            memberService.chargeMemberPoint(reqDTO);
        });
    }
}
