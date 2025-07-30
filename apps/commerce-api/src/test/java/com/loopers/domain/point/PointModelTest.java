package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PointModelTest {

    @DisplayName("포인트 객체를 생성할 때, ")
    @Nested
    class Create {

        static Stream<BigDecimal> negativeAmounts() {
            return Stream.of(
                    new BigDecimal("-1"),
                    new BigDecimal("-100.50"),
                    new BigDecimal("-0.01")
            );
        }

        @DisplayName("포인트 금액이 0 미만이면 예외를 발생시킨다.")
        @MethodSource("negativeAmounts")
        @ParameterizedTest
        void failCreate_whenAmountLessThanZero(BigDecimal amount) {
            Long memberId = 1L;

            // 예외 발생 테스트
            assertThrows(CoreException.class, () ->
                    Point.create(memberId, amount)
            );
        }

        @DisplayName("회원 ID가 유효하지 않으면 예외를 발생시킨다.")
        @Test
        void failCreate_whenMemberIdIsInvalid() {
            Long memberId = null;
            BigDecimal amount = new BigDecimal("100");

            // 예외 발생 테스트
            assertThrows(CoreException.class, () ->
                    Point.create(memberId, amount)
            );
        }

        @DisplayName("모든 값이 유효하면 포인트 객체를 생성한다.")
        @Test
        void successCreate_whenAllValuesAreValid() {
            Long memberId = 1L;
            BigDecimal amount = new BigDecimal("100");

            // 포인트 객체 생성 테스트
            Point point = Point.create(memberId, amount);

            // 포인트 객체가 null이 아니고, 값이 올바른지 확인
            assertNotNull(point);
            assertEquals(memberId, point.getMemberId());
            assertEquals(amount, point.getAmount());
        }

    }

    @DisplayName("포인트를 사용할 때, ")
    @Nested
    class Use {

        @DisplayName("사용할 포인트 금액이 0 미만이면 예외를 발생시킨다.")
        @Test
        void failUse_whenAmountLessThanOrEqualToZero(
        ) {
            Point point = Point.create(1L, new BigDecimal("100"));

            // 예외 발생 테스트
            assertThrows(CoreException.class, () ->
                    point.use(BigDecimal.valueOf(0), 1)
            );
        }

        @DisplayName("수량이 0 이하면 예외를 발생시킨다.")
        @Test
        void failUse_whenQuantityLessThanOrEqualToZero() {
            Point point = Point.create(1L, new BigDecimal("100"));

            // 예외 발생 테스트
            assertThrows(CoreException.class, () ->
                    point.use(BigDecimal.valueOf(10), 0)
            );
        }

        @DisplayName("사용할 포인트 금액이 현재 포인트 금액보다 크면 예외를 발생시킨다.")
        @Test
        void failUse_whenAmountExceedsCurrentPoints() {
            Point point = Point.create(1L, new BigDecimal("100"));

            // 예외 발생 테스트
            assertThrows(CoreException.class, () ->
                    point.use(new BigDecimal("200"), 1)
            );
        }

    }

}
