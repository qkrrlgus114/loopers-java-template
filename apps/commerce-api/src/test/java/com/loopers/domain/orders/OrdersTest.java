package com.loopers.domain.orders;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdersTest {

    @DisplayName("주문을 생성할 때, ")
    @Nested
    class register {

        @DisplayName("회원 ID가 유효하지 않으면, 예외를 발생시킨다.")
        @Test
        void throwException_whenInvalidMemberId() {
            // given
            Long memberId = null;
            int quantity = 2;

            // when & then
            assertThrows(CoreException.class, () -> {
                Orders.create(memberId, quantity);
            });
        }

        @DisplayName("수량이 0 이하이면, 예외를 발생시킨다.")
        @Test
        void throwException_whenInvalidQuantity() {
            // given
            Long memberId = 1L;
            int quantity = 0;


            // when & then
            assertThrows(CoreException.class, () -> {
                Orders.create(memberId, quantity);
            });
        }

        @DisplayName("모든 값이 유효하면, 주문을 생성한다.")
        @Test
        void createOrder_whenAllValuesAreValid() {
            // given
            Long memberId = 1L;
            int quantity = 2;


            // when
            Orders orders = Orders.create(memberId, quantity);

            // then
            assertEquals(memberId, orders.getMemberId());
            assertEquals(OrderStatus.PENDING, orders.getOrderStatus());
            assertEquals(quantity, orders.getQuantity());
        }

    }

}
