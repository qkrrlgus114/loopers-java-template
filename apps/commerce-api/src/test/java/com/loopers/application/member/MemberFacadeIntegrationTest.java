package com.loopers.application.member;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MemberFacadeIntegrationTest {

    @Autowired
    private MemberFacade memberFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private PointRepository pointRepository;

    MemberRegisterCommand command;

    @BeforeEach
    void setUp() {
        command = MemberRegisterCommand.of(
                "test",
                "test",
                "test@naver.com",
                "박기현",
                "1997-12-04",
                "M"
        );
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Test
    @DisplayName("유효한 회원 정보를 주면, 회원가입에 성공한다.")
    void success_whenRegisterCollectData() {
        MemberRegisterResult memberRegisterResult = memberFacade.registerMember(command);

        Point point = pointRepository.findByMemberId(memberRegisterResult.id()).get();

        assertAll(
                "회원 가입 결과 및 포인트 생성 검증",
                () -> assertThat(memberRegisterResult).isNotNull(),
                () -> assertThat(memberRegisterResult.id()).isNotNull(),
                () -> assertThat(memberRegisterResult.loginId()).isEqualTo(command.loginId()),
                () -> assertThat(memberRegisterResult.email()).isEqualTo(command.email()),
                () -> assertThat(memberRegisterResult.name()).isEqualTo(command.name()),
                () -> assertThat(LocalDate.parse(memberRegisterResult.birth())).isEqualTo(command.birth()),
                () -> assertThat(memberRegisterResult.gender()).isEqualTo(command.gender()),
                () -> assertThat(point).isNotNull(),
                () -> assertThat(point.getMemberId()).isEqualTo(memberRegisterResult.id()),
                () -> assertThat(point.getAmount()).isEqualByComparingTo(BigDecimal.ZERO)
        );
    }

    @Test
    @DisplayName("이미 가입된 회원 ID로 재가입 시도 시 실패한다.")
    void fail_whenRegisterWithDuplicateLoginId() {
        // given: 첫 번째 회원가입 성공
        memberFacade.registerMember(command);

        // when & then: 동일한 loginId로 재가입 시도
        MemberRegisterCommand duplicateCommand = MemberRegisterCommand.of(
                "test",  // 동일한 loginId
                "test2",
                "test2@naver.com",
                "김철수",
                "1995-05-15",
                "M"
        );

        assertThatThrownBy(() -> memberFacade.registerMember(duplicateCommand))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorType", MemberErrorType.FAIL_REGISTER)
                .hasMessageContaining("회원가입에 실패했습니다");
    }
}
