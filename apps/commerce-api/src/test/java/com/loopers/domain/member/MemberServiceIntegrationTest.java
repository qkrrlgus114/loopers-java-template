package com.loopers.domain.member;

import com.loopers.application.member.MemberRegisterInfo;
import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

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
            // given
            MemberRegisterReqDTO memberRegisterReqDTO = MemberRegisterReqDTO.builder()
                    .loginId("test")
                    .password("test")
                    .email("test@naver.com")
                    .name("박기현")
                    .gender("M")
                    .birth("1997-12-04").build();

            // when
            MemberRegisterInfo saved = memberService.register(memberRegisterReqDTO);

            // then
            assertEquals(saved.loginId(), memberRegisterReqDTO.getLoginId());
            assertEquals(saved.email(), memberRegisterReqDTO.getEmail());
            assertEquals(saved.name(), memberRegisterReqDTO.getName());
            assertEquals(saved.birth(), memberRegisterReqDTO.getBirth());
        }

        @DisplayName("이미 가입된 아이디로 가입하면, 회원가입을 실패한다.")
        @Test
        void fail_conflictLoginIdRegister() {
            // given
            MemberRegisterReqDTO memberRegisterReqDTO1 = MemberRegisterReqDTO.builder()
                    .loginId("test")
                    .password("test")
                    .email("test@naver.com")
                    .name("박기현")
                    .gender("M")
                    .birth("1997-12-04").build();
            MemberRegisterReqDTO memberRegisterReqDTO2 = MemberRegisterReqDTO.builder()
                    .loginId("test")
                    .password("1234")
                    .email("1234@naver.com")
                    .name("박기현")
                    .gender("M")
                    .birth("1997-12-04").build();
            MemberRegisterInfo firstSaved = memberService.register(memberRegisterReqDTO1);

            assertThrows(CoreException.class, () -> {
                memberService.register(memberRegisterReqDTO2);
            });
        }

    }
}
