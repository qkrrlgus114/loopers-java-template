package com.loopers.interfaces.api;

import com.loopers.interfaces.api.brand.dto.BrandDTO;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandV1ApiE2ETest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @DisplayName("브랜드를 등록할 때, ")
    @Nested
    class Register {

        @AfterEach
        void tearDown() {
            databaseCleanUp.truncateAllTables();
        }

        @DisplayName("브랜드 이름이 유효하지 않으면 등록에 실패한다.")
        @NullAndEmptySource
        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "20자가넘는브랜드이름20자가넘는브랜드이름20자가넘는브랜드이름20자가넘는브랜드이름"})
        void fail_whenBrandNameIsInvalid(String name) {
            // given
            String description = "브랜드 설명";
            BrandDTO.BrandRegisterRequest reqDTO = new BrandDTO.BrandRegisterRequest(
                    name,
                    description,
                    1L
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "test");
            HttpEntity httpEntity = new HttpEntity(reqDTO, headers);

            // when
            ResponseEntity<ApiResponse<BrandDTO.BrandRegisterResponse>> response = testRestTemplate.exchange(
                    "/api/v1/brands", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );

        }

        @DisplayName("브랜드 설명이 유효하지 않으면 등록에 실패한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void fail_whenBrandDescriptionIsInvalid(String description) {
            // given
            String name = "브랜드 이름";
            BrandDTO.BrandRegisterRequest reqDTO = new BrandDTO.BrandRegisterRequest(
                    name,
                    description,
                    1L
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "test");
            HttpEntity httpEntity = new HttpEntity(reqDTO, headers);

            // when
            ResponseEntity<ApiResponse<BrandDTO.BrandRegisterResponse>> response = testRestTemplate.exchange(
                    "/api/v1/brands", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }

        @DisplayName("유효한 브랜드 정보를 주면, 브랜드 등록에 성공한다.")
        @Test
        void success_whenValidBrandInfo() {
            // given
            String name = "유효한 브랜드 이름";
            String description = "유효한 브랜드 설명";
            Long memberId = 1L;
            BrandDTO.BrandRegisterRequest reqDTO = new BrandDTO.BrandRegisterRequest(
                    name,
                    description,
                    memberId
            );
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "test");
            HttpEntity<BrandDTO.BrandRegisterRequest> httpEntity = new HttpEntity<>(reqDTO, headers);

            // when
            ResponseEntity<ApiResponse<BrandDTO.BrandRegisterResponse>> response = testRestTemplate.exchange(
                    "/api/v1/brands", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().name()).isEqualTo(name),
                    () -> assertThat(response.getBody().data().description()).isEqualTo(description),
                    () -> assertThat(response.getBody().data().memberId()).isEqualTo(memberId)
            );
        }

    }
}
