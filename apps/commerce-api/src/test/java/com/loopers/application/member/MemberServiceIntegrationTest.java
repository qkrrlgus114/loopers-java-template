package com.loopers.application.member;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.command.PointChargeCommand;
import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.application.member.result.MemberPointResult;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.interfaces.api.member.dto.MemberDTO;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

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

    @Nested
    @DisplayName("회원가입을 진행할 때, ")
    class Register {

        @DisplayName("유효한 회원 정보를 주면, 회원가입에 성공한다.")
        @Test
        void success_whenRegisterCollectDate() {
            // when
            MemberRegisterResult saved = memberService.register(memberRegisterCommand);

            // then
            assertAll(
                    () -> assertThat(saved).isNotNull(),
                    () -> assertThat(saved.id()).isNotNull(),
                    () -> assertThat(saved.loginId()).isEqualTo(memberRegisterCommand.loginId()),
                    () -> assertThat(saved.email()).isEqualTo(memberRegisterCommand.email()),
                    () -> assertThat(saved.name()).isEqualTo(memberRegisterCommand.name()),
                    () -> assertThat(LocalDate.parse(saved.birth())).isEqualTo(memberRegisterCommand.birth())
            );
        }

        @DisplayName("이미 가입된 아이디로 가입하면, 회원가입을 실패한다.")
        @Test
        void fail_conflictLoginIdRegister() {
            // given
            MemberDTO.RegisterRequest memberRegisterReqDTO = MemberDTO.RegisterRequest.builder()
                    .loginId("test")
                    .password("1234")
                    .email("1234@naver.com")
                    .name("박기현")
                    .gender("M")
                    .birth("1997-12-04").build();
            MemberRegisterCommand memberRegisterCommand1 = MemberRegisterCommand.of(
                    memberRegisterReqDTO.getLoginId(),
                    memberRegisterReqDTO.getPassword(),
                    memberRegisterReqDTO.getEmail(),
                    memberRegisterReqDTO.getName(),
                    memberRegisterReqDTO.getBirth(),
                    memberRegisterReqDTO.getGender()
            );
            memberService.register(memberRegisterCommand);

            assertThrows(CoreException.class, () -> {
                memberService.register(memberRegisterCommand1);
            });
        }

    }

    @Nested
    @DisplayName("해당 ID의 회원이 ")
    class GetMemberInfo {

        @DisplayName("존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnMemberInfo_whenMemberExists() {
            MemberRegisterResult saved = memberService.register(memberRegisterCommand);

            String memberId = String.valueOf(saved.id());
            // when
            MemberInfoResult memberInfoResult = memberService.getMemberInfo(memberId);

            // then
            assertAll(
                    () -> assertThat(memberInfoResult).isNotNull(),
                    () -> assertThat(memberInfoResult.loginId()).isEqualTo(memberRegisterCommand.loginId()),
                    () -> assertThat(memberInfoResult.email()).isEqualTo(memberRegisterCommand.email()),
                    () -> assertThat(memberInfoResult.name()).isEqualTo(memberRegisterCommand.name()),
                    () -> assertThat(memberInfoResult.birth()).isEqualTo(memberRegisterCommand.birth()),
                    () -> assertThat(memberInfoResult.gender()).isEqualTo(memberRegisterCommand.gender())
            );
        }

        @DisplayName("존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenMemberDoesNotExist() {
            // given
            String memberId = "9999";

            // when
            MemberInfoResult memberInfoResult = memberService.getMemberInfo(memberId);

            // then
            assertNull(memberInfoResult);
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
            MemberRegisterResult saved = memberService.register(memberRegisterCommand);

            // when
            MemberPointResult memberPointResult = memberService.getMemberPoint(String.valueOf(saved.id()));

            // then
            assertAll(
                    () -> assertThat(memberPointResult).isNotNull(),
                    () -> assertThat(memberPointResult.memberId()).isEqualTo(saved.id()),
                    () -> assertThat(memberPointResult.point()).isEqualTo(0)
            );
        }

        @DisplayName("존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenMemberDoesNotExist() {
            // given
            String memberId = "9999";

            // when
            MemberPointResult memberPointResult = memberService.getMemberPoint(memberId);

            // then
            assertNull(memberPointResult);
        }
    }

    @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
    @Test
    void fail_whenChargePointWithNonExistentUserId() {
        // given
        PointChargeCommand pointChargeCommand = new PointChargeCommand(
                1L,
                10000L
        );


        // when & then
        assertThrows(CoreException.class, () -> {
            memberService.chargeMemberPoint(pointChargeCommand);
        });
    }
}
