package com.loopers.domain.member;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberModelTest {

    @DisplayName("회원가입을 진행할 때, ")
    @Nested
    class Register {
        @DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @Test
        void failRegister_whenIdNotMatchPattern() {
            String loginId = "12312321521321";
            String password = "12341234";
            String email = "test@naver.com";
            String name = "박기현";
            String birth = "1997-12-04";
            String gender = "M";

            assertThatThrownBy(() ->
                    new MemberModel(loginId, password, email, name, birth, gender))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아이디는 [영문 + 숫자] 10자 이하여야 합니다.");
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @Test
        void failRegister_whenEmailNotMatchPattern() {
            String loginId = "test1234";
            String password = "12341234";
            String email = "testnaver.com";
            String name = "박기현";
            String birth = "1997-12-04";
            String gender = "M";

            assertThatThrownBy(() ->
                    new MemberModel(loginId, password, email, name, birth, gender))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이메일 형식이 일치하지 않습니다.");
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @Test
        void failRegister_whenBirthNotMatchPattern() {
            String loginId = "test1234";
            String password = "12341234";
            String email = "test@naver.com";
            String name = "박기현";
            String birth = "19927-12-04";
            String gender = "M";

            assertThatThrownBy(() ->
                    new MemberModel(loginId, password, email, name, birth, gender))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("생년월일 형식에 문제가 발생했습니다.");
        }
    }

    @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
    @Test
    void fail_whenChargePointZeroOrLess() {
        // given
        MemberModel memberModel = new MemberModel(
                "id", "password", "test@naver.com", "name", "1997-12-04", "M"
        );
        Long amount = 0L;

        // when && then
        Assertions.assertThrows(CoreException.class, () -> {
            memberModel.chargePoint(amount);
        });
    }

}
