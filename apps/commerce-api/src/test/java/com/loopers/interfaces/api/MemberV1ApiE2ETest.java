package com.loopers.interfaces.api;

import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberInfoResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberV1ApiE2ETest {

    /*
     * - [ ]  회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.
     * - [ ]  회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.
     * */

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Register {

        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnMemberRegisterResDTO_whenRegisterMemberSuccessful() {
            // given
            MemberRegisterReqDTO reqDTO = MemberRegisterReqDTO.builder()
                    .loginId("testUser")
                    .password("testPassword")
                    .email("test@naver.com")
                    .birth("1997-12-04")
                    .name("박기현")
                    .gender("M").build();

            // when
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> response = testRestTemplate.exchange("/api/v1/users", HttpMethod.POST,
                    new HttpEntity<>(reqDTO), new ParameterizedTypeReference<ApiResponse<MemberRegisterResDTO>>() {
                    });


            // then
            ApiResponse<MemberRegisterResDTO> body = response.getBody();
            MemberRegisterResDTO memberRegisterResDTO = body.data() != null ? body.data() : null;
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(body).isNotNull(),
                    () -> assertThat(memberRegisterResDTO.getLoginId()).isEqualTo(reqDTO.getLoginId()),
                    () -> assertThat(memberRegisterResDTO.getEmail()).isEqualTo(reqDTO.getEmail()),
                    () -> assertThat(memberRegisterResDTO.getName()).isEqualTo(reqDTO.getName()),
                    () -> assertThat(memberRegisterResDTO.getBirth()).isEqualTo(reqDTO.getBirth()),
                    () -> assertThat(memberRegisterResDTO.getGender()).isEqualTo(reqDTO.getGender())
            );

        }

        @DisplayName("회원 가입 시에 성별이 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void returnBadRequest_whenGenderIsMissing() {
            // given
            MemberRegisterReqDTO reqDTO = MemberRegisterReqDTO.builder()
                    .loginId("testUser")
                    .password("testPassword")
                    .email("test@naver.com")
                    .birth("1997-12-04")
                    .name("박기현")
                    .gender(null)
                    .build();

            // when
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> response = testRestTemplate.exchange("/api/v1/users", HttpMethod.POST, new HttpEntity<>(reqDTO),
                    new ParameterizedTypeReference<ApiResponse<MemberRegisterResDTO>>() {
                    });

            // then
            assertAll(
                    () -> assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode())
            );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users/me")
    class GetMyInfo {

        /*
         * - [ ]  내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.
         * - [ ]  존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.
         * */

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnMemberInfo_whenGetMyInfoSuccessful() {
            // given
            MemberRegisterReqDTO reqDTO = MemberRegisterReqDTO.builder()
                    .loginId("testUser")
                    .password("testPassword")
                    .email("test@naver.com")
                    .birth("1997-12-04")
                    .name("박기현")
                    .gender("M").build();
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> registerResponse = testRestTemplate.
                    exchange("/api/v1/users", HttpMethod.POST, new HttpEntity<>(reqDTO), new ParameterizedTypeReference<>() {
                    });

            // when
            ResponseEntity<ApiResponse<MemberInfoResDTO>> response = testRestTemplate.exchange(
                    "/api/v1/users/me?memberId=" + registerResponse.getBody().data().getId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> {
                        MemberInfoResDTO memberInfo = response.getBody().data();
                        assertThat(memberInfo.getLoginId()).isEqualTo(reqDTO.getLoginId());
                        assertThat(memberInfo.getEmail()).isEqualTo(reqDTO.getEmail());
                        assertThat(memberInfo.getName()).isEqualTo(reqDTO.getName());
                        assertThat(memberInfo.getBirth()).isEqualTo(reqDTO.getBirth());
                        assertThat(memberInfo.getGender()).isEqualTo(reqDTO.getGender());
                    }
            );
        }

        @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returnNotFound_whenMemberIdDoesNotExist() {
            // given
            String memberId = "9999";

            // when
            ResponseEntity<ApiResponse<MemberInfoResDTO>> response =
                    testRestTemplate.exchange("/api/v1/users/me?memberId=" + memberId, HttpMethod.GET, null,
                            new ParameterizedTypeReference<>() {
                            });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().meta()).isNotNull(),
                    () -> assertThat(response.getBody().meta().message()).contains("회원 정보를 찾을 수 없습니다. 회원 ID: " + memberId)
            );

        }
    }
}
