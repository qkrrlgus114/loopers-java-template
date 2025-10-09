package com.loopers.application.member;

import com.loopers.application.member.command.MemberRegisterCommand;
import com.loopers.application.member.result.MemberRegisterResult;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MemberFacadeIntegrationTest {

    @Autowired
    private MemberFacade memberFacade;

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
}
