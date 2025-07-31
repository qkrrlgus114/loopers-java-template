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

        @DisplayName("상품 ID가 유효하지 않으면, 예외를 발생시킨다.")
        @Test
        void throwException_whenInvalidProductId() {
            // given
            Long productId = null;
            Long memberId = 1L;
            int quantity = 2;
            String orderKey = "test";

            // when & then
            assertThrows(CoreException.class, () -> {
                Orders.create(memberId, productId, quantity, orderKey);
            });
        }

        @DisplayName("회원 ID가 유효하지 않으면, 예외를 발생시킨다.")
        @Test
        void throwException_whenInvalidMemberId() {
            // given
            Long productId = 1L;
            Long memberId = null;
            int quantity = 2;
            String orderKey = "test";

            // when & then
            assertThrows(CoreException.class, () -> {
                Orders.create(memberId, productId, quantity, orderKey);
            });
        }

        @DisplayName("수량이 0 이하이면, 예외를 발생시킨다.")
        @Test
        void throwException_whenInvalidQuantity() {
            // given
            Long productId = 1L;
            Long memberId = 1L;
            int quantity = 0;
            String orderKey = "test";

            // when & then
            assertThrows(CoreException.class, () -> {
                Orders.create(memberId, productId, quantity, orderKey);
            });
        }

        @DisplayName("모든 값이 유효하면, 주문을 생성한다.")
        @Test
        void createOrder_whenAllValuesAreValid() {
            // given
            Long productId = 1L;
            Long memberId = 1L;
            int quantity = 2;
            String orderKey = "test";

            // when
            Orders orders = Orders.create(memberId, productId, quantity, orderKey);

            // then
            assertEquals(memberId, orders.getMemberId());
            assertEquals(productId, orders.getProductId());
            assertEquals(OrderStatus.PENDING, orders.getOrderStatus());
            assertEquals(quantity, orders.getQuantity());
        }

    }

}
