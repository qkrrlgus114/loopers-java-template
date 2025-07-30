package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ProductModelTest {

    @DisplayName("상품을 등록할 때, ")
    @Nested
    class Register {

        @DisplayName("상품 이름이 1자 이상 20자 이내가 아니면 Product 객체 생성에 실패한다.")
        @NullAndEmptySource
        @ParameterizedTest
        @ValueSource(strings = {
                "abcdeabcdeabcdeabcdeabcde"
        })
        void failRegister_whenNameNotMatchPattern(String name) {
            String description = "상품 설명";
            Long brandId = 1L;
            Long memberId = 1L;
            BigDecimal price = BigDecimal.valueOf(1000);

            assertThrows(
                    CoreException.class,
                    () -> ProductModel.create(name, description, brandId, memberId, price)
            );
        }

        @DisplayName("상품 설명이 20자 이상 200자 이내가 아니면 Product 객체 생성에 실패한다.")
        @ParameterizedTest
        @MethodSource("invalidDescriptions")
        void failRegister_whenDescriptionNotMatchPattern(String description) {
            String name = "상품 이름";
            Long brandId = 1L;
            Long memberId = 1L;
            BigDecimal price = BigDecimal.valueOf(1000);

            assertThrows(
                    CoreException.class,
                    () -> ProductModel.create(name, description, brandId, memberId, price)
            );
        }

        static Stream<String> invalidDescriptions() {
            return Stream.of(
                    "",
                    "짧은 설명",
                    "a".repeat(201)
            );
        }

        @DisplayName("브랜드 ID가 유효하지 않으면 Product 객체 생성에 실패한다.")
        @ParameterizedTest
        @MethodSource("invalidBrandIds")
        void failRegister_whenBrandIdNotValid(Long brandId) {
            String name = "상품 이름";
            String description = "상품 설명";
            Long memberId = 1L;
            BigDecimal price = BigDecimal.valueOf(1000);

            assertThrows(
                    CoreException.class,
                    () -> ProductModel.create(name, description, brandId, memberId, price)
            );
        }

        static Stream<Long> invalidBrandIds() {
            return Stream.of(null, 0L, -1L, -100L);
        }

        @DisplayName("회원 ID가 유효하지 않으면 Product 객체 생성에 실패한다.")
        @ParameterizedTest
        @MethodSource("invalidMemberIds")
        void failRegister_whenMemberIdNotValid(Long memberId) {
            String name = "상품 이름";
            String description = "상품 설명";
            Long brandId = 1L;
            BigDecimal price = BigDecimal.valueOf(1000);

            assertThrows(
                    CoreException.class,
                    () -> ProductModel.create(name, description, brandId, memberId, price)
            );
        }

        static Stream<Long> invalidMemberIds() {
            return Stream.of(null, 0L, -1L, -100L);
        }

        @DisplayName("상품 가격이 0 이하이면 Product 객체 생성에 실패한다.")
        @ParameterizedTest
        @MethodSource("invalidPrices")
        void failRegister_whenPriceNotValid(BigDecimal price) {
            String name = "상품 이름";
            String description = "상품 설명";
            Long brandId = 1L;
            Long memberId = 1L;

            assertThrows(
                    CoreException.class,
                    () -> ProductModel.create(name, description, brandId, memberId, price)
            );
        }

        static Stream<BigDecimal> invalidPrices() {
            return Stream.of(
                    BigDecimal.ZERO,
                    BigDecimal.valueOf(-1),
                    BigDecimal.valueOf(-100)
            );
        }

        @DisplayName("모든 조건이 만족되면 Product 객체가 생성된다.")
        @Test
        void successRegister_whenAllConditionsMet() {
            String name = "상품 이름";
            String description = "상품 설명은 20자여야 합니다.상품 설명은 20자여야 합니다.상품 설명은 20자여야 합니다.상품 설명은 20자여야 합니다.";
            Long brandId = 1L;
            Long memberId = 1L;
            BigDecimal price = BigDecimal.valueOf(1000);

            ProductModel productModel = ProductModel.create(name, description, brandId, memberId, price);

            assertNotNull(productModel);
            assertEquals(name, productModel.getName());
            assertEquals(description, productModel.getDescription());
            assertEquals(brandId, productModel.getBrandId());
            assertEquals(memberId, productModel.getMemberId());
            assertEquals(price, productModel.getPrice());
            assertEquals(ProductStatus.REGISTERED, productModel.getStatus());
        }
    }


}
