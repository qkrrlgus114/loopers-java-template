package com.loopers.domain.member;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberTest {

    @DisplayName("회원가입을 진행할 때, ")
    @Nested
    class Register {

        @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "12345678901",
                "1234asdf#",
                "!@#$%!@#%!@#$@!",
                "qkrrlgus114",
                "qkrrlgus114##"
        })
        void failRegister_whenIdNotMatchPattern(String loginId) {
            String password = "12341234";
            String email = "test@naver.com";
            String name = "박기현";
            LocalDate birth = LocalDate.parse("1997-12-04");
            String gender = "M";

            assertThrows(
                    CoreException.class,
                    () -> Member.registerMember(loginId, password, email, name, birth, gender)
            );
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "testnaver.com",
                "test@navercom",
                "test@naver..com",
                "test@.com",
                "@naver.com",
                "test@naver.c"
        })
        void failRegister_whenEmailNotMatchPattern(String email) {
            String loginId = "test1234";
            String password = "12341234";
            String name = "박기현";
            LocalDate birth = LocalDate.parse("1997-12-04");
            String gender = "M";

            assertThrows(
                    CoreException.class,
                    () -> Member.registerMember(loginId, password, email, name, birth, gender)
            );
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, Command 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "1997/12/04",
                "1997.12.04",
                "1997-12-4",
                "1997-12-40",
                "1997-13-04",
                "1997-00-04",
                "1997-12-32"
        })
        void failCreateCommand_whenBirthNotMatchPattern(String birthStr) {
            String loginId = "test1234";
            String password = "12341234";
            String email = "test@naver.com";
            String name = "박기현";
            String gender = "M";

            assertThrows(
                    CoreException.class,
                    () -> MemberRegisterCommand.of(loginId, password, email, name, birthStr, gender)
            );
        }

        @DisplayName("성별이 M 또는 F가 아니면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "M1",
                "F2",
        })
        void failRegister_whenGenderNotMatchPattern(String gender) {
            String loginId = "test1234";
            String password = "12341234";
            String email = "test@naver.com";
            String name = "박기현";
            LocalDate birth = LocalDate.parse("1997-12-04");

            assertThrows(
                    CoreException.class,
                    () -> Member.registerMember(loginId, password, email, name, birth, gender)
            );
        }
    }

    @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
    @Test
    void fail_whenChargePointZeroOrLess() {
        // given
        Member member = Member.registerMember("test1234", "12341234", "test@naver.com", "박기현", LocalDate.parse("1997-12-04"), "M");
        Long amount = 0L;

        // when && then
        assertThrows(CoreException.class, () -> {
            member.chargePoint(amount);
        });
    }

}
