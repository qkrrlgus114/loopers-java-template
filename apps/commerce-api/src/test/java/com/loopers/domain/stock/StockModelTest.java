package com.loopers.domain.stock;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StockModelTest {

    @DisplayName("재고를 생성할 때, ")
    @Nested
    class Register {

        @DisplayName("상품 ID가 null이거나 0 이하인 경우 실패한다.")
        @Test
        void fail_productIdNullOrZero() {
            Long productId = null;
            int quantity = 10;

            Assertions.assertThrows(CoreException.class,
                    () -> StockModel.create(productId, quantity));
        }

        @DisplayName("수량이 0 미만인 경우 실패한다.")
        @Test
        void fail_quantityLessThanZero() {
            Long productId = 1L;
            int quantity = -1;

            Assertions.assertThrows(CoreException.class,
                    () -> StockModel.create(productId, quantity));
        }

        @DisplayName("유효한 상품 ID와 수량으로 재고를 생성할 수 있다.")
        @Test
        void success_createStock() {
            Long productId = 1L;
            int quantity = 10;

            StockModel stock = StockModel.create(productId, quantity);

            assertNotNull(stock);
            assertEquals(productId, stock.getProductId());
            assertEquals(quantity, stock.getQuantity());
        }
    }

    @DisplayName("재고 수량을 감소시킬 때, ")
    @Nested
    class DecreaseQuantity {

        @DisplayName("감소할 수량이 0 이하인 경우 실패한다.")
        @Test
        void fail_decreaseQuantityZeroOrLess() {
            Long productId = 1L;
            int quantity = 10;
            StockModel stock = StockModel.create(productId, quantity);

            Assertions.assertThrows(CoreException.class,
                    () -> stock.decreaseQuantity(0));
        }

        @DisplayName("재고 수량이 부족한 경우 실패한다.")
        @Test
        void fail_insufficientStock() {
            Long productId = 1L;
            int quantity = 10;
            StockModel stock = StockModel.create(productId, quantity);

            Assertions.assertThrows(CoreException.class,
                    () -> stock.decreaseQuantity(11));
        }

        @DisplayName("유효한 수량으로 재고를 감소시킬 수 있다.")
        @Test
        void success_decreaseQuantity() {
            Long productId = 1L;
            int quantity = 10;
            StockModel stock = StockModel.create(productId, quantity);

            stock.decreaseQuantity(5);
            assertEquals(5, stock.getQuantity());
        }
    }
}
