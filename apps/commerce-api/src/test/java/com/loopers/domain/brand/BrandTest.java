package com.loopers.domain.brand;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BrandTest {

    @DisplayName("브랜드 등록을 진행할 때, ")
    @Nested
    class Register {

        @DisplayName("이름 규칙에 맞지 않으면 Brand 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "134584137894368943157894135",
                "",
                " "
        })
        void failRegister_whenNameNotMatchPattern(String name) {
            String description = "테스트 브랜드 설명";
            Long memberId = 1L;

            assertThrows(
                    CoreException.class,
                    () -> Brand.create(name, description, memberId)
            );
        }

        @DisplayName("설명 규칙에 맞지 않으면 Brand 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "",
                " ",
                "asdfsadddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
        })
        void failRegister_whenDescriptionNotMatchPattern(String description) {
            String name = "테스트브랜드";
            Long memberId = 1L;

            assertThrows(
                    CoreException.class,
                    () -> Brand.create(name, description, memberId)
            );
        }

        @DisplayName("모든 값이 유효하면 Brand 객체 생성에 성공한다.")
        @Test
        void successRegister_whenAllValuesAreValid() {
            String name = "테스트브랜드";
            String description = "테스트 브랜드 설명";
            Long memberId = 1L;

            Brand brand = Brand.create(name, description, memberId);

            assertEquals(name, brand.getName());
            assertEquals(description, brand.getDescription());
        }
    }


}
