package com.loopers.domain.productlike;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductLikeTest {

    @DisplayName("좋아요를 생성할 때")
    @Nested
    class CreateProductLike {

        @DisplayName("정상적으로 생성된다")
        @Test
        void createProductLike_success() {
            // Given
            Long productId = 1L;
            Long memberId = 1L;

            // When
            ProductLike productLike = ProductLike.create(productId, memberId);

            // Then
            assertNotNull(productLike);
            assertEquals(productId, productLike.getProductId());
            assertEquals(memberId, productLike.getMemberId());
        }

        @DisplayName("상품 ID가 유효하지 않으면 예외가 발생한다")
        @Test
        void createProductLike_invalidProductId() {
            // Given
            Long productId = null;
            Long memberId = 1L;

            // When & Then
            assertThrows(CoreException.class, () -> ProductLike.create(productId, memberId));
        }

        @DisplayName("회원 ID가 유효하지 않으면 예외가 발생한다")
        @Test
        void createProductLike_invalidMemberId() {
            // Given
            Long productId = 1L;
            Long memberId = null;

            // When & Then
            assertThrows(CoreException.class, () -> ProductLike.create(productId, memberId));
        }
    }
}
