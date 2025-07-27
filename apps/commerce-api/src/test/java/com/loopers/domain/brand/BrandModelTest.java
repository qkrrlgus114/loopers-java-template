package com.loopers.domain.brand;

import com.loopers.domain.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BrandModelTest {

    private Image image;

    @DisplayName("브랜드 등록을 진행할 때, ")
    @Nested
    class Register {

        @BeforeEach
        void setUp() {
            image = new Image("http://example.com/image.jpg", "http://example.com/thumbnail.jpg");
        }

        @DisplayName("이름 규칙에 맞지 않으면 Brand 객체 생성에 실패한다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "134584137894368943157894135",
                "",
                " "
        })
        void failRegister_whenNameNotMatchPattern(String name) {
            String description = "테스트 브랜드 설명";

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BrandModel.registerBrand(name, description, image)
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

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BrandModel.registerBrand(name, description, image)
            );
        }

        @DisplayName("이미지가 null이면 Brand 객체 생성에 실패한다.")
        @Test
        void failRegister_whenImageIsNull() {
            String name = "테스트브랜드";
            String description = "테스트 브랜드 설명";
            image = null;

            assertThrows(
                    IllegalArgumentException.class,
                    () -> BrandModel.registerBrand(name, description, image)
            );
        }

        @DisplayName("모든 값이 유효하면 Brand 객체 생성에 성공한다.")
        @Test
        void successRegister_whenAllValuesAreValid() {
            String name = "테스트브랜드";
            String description = "테스트 브랜드 설명";

            BrandModel brandModel = BrandModel.registerBrand(name, description, image);

            assertEquals(name, brandModel.getName());
            assertEquals(description, brandModel.getDescription());
            assertEquals(image, brandModel.getImage());
        }
    }


}
