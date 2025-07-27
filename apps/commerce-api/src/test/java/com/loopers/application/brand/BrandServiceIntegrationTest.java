package com.loopers.application.brand;

import com.loopers.application.brand.command.BrandRegisterCommand;
import com.loopers.application.brand.command.BrandUpdateCommand;
import com.loopers.application.brand.result.BrandRegisterResult;
import com.loopers.application.brand.result.BrandUpdateResult;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class BrandServiceIntegrationTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    private BrandRegisterCommand brandRegisterCommand;

    @BeforeEach
    public void setUp() {
        brandRegisterCommand = new BrandRegisterCommand(
                "브랜드이름",
                "브랜드 설명입니다아아아아",
                1L
        );
    }

    @Nested
    @DisplayName("브랜드 등록을 진행할 때, ")
    class Register {

        @DisplayName("브랜드 등록이 성공하면 브랜드 정보를 반환한다.")
        @Test
        void success_whenRegisterCollectDate() {
            BrandRegisterResult saved = brandService.registerBrand(brandRegisterCommand);

            assertAll(
                    () -> assertThat(saved).isNotNull(),
                    () -> assertThat(saved.id()).isNotNull(),
                    () -> assertThat(saved.name()).isEqualTo(brandRegisterCommand.name()),
                    () -> assertThat(saved.description()).isEqualTo(brandRegisterCommand.description()),
                    () -> assertThat(saved.memberId()).isEqualTo(brandRegisterCommand.memberId())
            );
        }

        @DisplayName("브랜드 이름이 1자 이상 20자 이하로 입력되지 않으면 예외가 발생한다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                "abcdefghijabcdefghijabcdefghij"
        })
        void fail_whenNameIsInvalid(String name) {
            BrandRegisterCommand invalidCommand = new BrandRegisterCommand(
                    name,
                    "브랜드 설명입니다아아아아",
                    1L
            );

            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> brandService.registerBrand(invalidCommand));
        }

        @DisplayName("브랜드 설명이 10자 이상 255자 이하로 입력되지 않으면 예외가 발생한다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
                "짧은",
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
        void fail_whenDescriptionIsInvalid(String description) {
            BrandRegisterCommand invalidCommand = new BrandRegisterCommand(
                    "브랜드이름",
                    description,
                    1L
            );

            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> brandService.registerBrand(invalidCommand));
        }


    }

    @DisplayName("브랜드를 수정할 때, ")
    @Nested
    class Update {

        @DisplayName("브랜드 정보가 올바르게 수정되면 수정된 브랜드 정보를 반환한다.")
        @Test
        void success_whenUpdateBrand() {
            // given
            BrandRegisterResult saved = brandService.registerBrand(brandRegisterCommand);
            Long brandId = saved.id();
            BrandUpdateCommand updateCommand = new BrandUpdateCommand(
                    brandId,
                    "수정된 브랜드 이름",
                    "수정된 브랜드 설명입니다.",
                    saved.memberId()
            );

            // when
            BrandUpdateResult brandUpdateResult = brandService.updateBrand(updateCommand);

            // then
            assertAll(
                    () -> assertThat(brandUpdateResult).isNotNull(),
                    () -> assertThat(brandUpdateResult.id()).isEqualTo(brandId),
                    () -> assertThat(brandUpdateResult.name()).isEqualTo(updateCommand.name()),
                    () -> assertThat(brandUpdateResult.description()).isEqualTo(updateCommand.description()),
                    () -> assertThat(brandUpdateResult.memberId()).isEqualTo(updateCommand.memberId())
            );
        }
    }
}
