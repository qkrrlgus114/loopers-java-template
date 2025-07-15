package com.loopers.interfaces.api;

import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberInfoResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberPointResDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

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

    private MemberRegisterReqDTO setUpMemberReqDTO;

    @BeforeEach
    void setUp() {
        setUpMemberReqDTO = MemberRegisterReqDTO.builder()
                .loginId("testUser")
                .password("testPassword")
                .email("test@naver.com")
                .birth("1997-12-04")
                .name("박기현")
                .gender("M").build();
    }


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
            // when
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> response = testRestTemplate.exchange("/api/v1/users", HttpMethod.POST,
                    new HttpEntity<>(setUpMemberReqDTO), new ParameterizedTypeReference<ApiResponse<MemberRegisterResDTO>>() {
                    });


            // then
            ApiResponse<MemberRegisterResDTO> body = response.getBody();
            MemberRegisterResDTO memberRegisterResDTO = body.data() != null ? body.data() : null;
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(body).isNotNull(),
                    () -> assertThat(memberRegisterResDTO.getLoginId()).isEqualTo(setUpMemberReqDTO.getLoginId()),
                    () -> assertThat(memberRegisterResDTO.getEmail()).isEqualTo(setUpMemberReqDTO.getEmail()),
                    () -> assertThat(memberRegisterResDTO.getName()).isEqualTo(setUpMemberReqDTO.getName()),
                    () -> assertThat(memberRegisterResDTO.getBirth()).isEqualTo(setUpMemberReqDTO.getBirth()),
                    () -> assertThat(memberRegisterResDTO.getGender()).isEqualTo(setUpMemberReqDTO.getGender())
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
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> registerResponse = testRestTemplate.
                    exchange("/api/v1/users", HttpMethod.POST, new HttpEntity<>(setUpMemberReqDTO), new ParameterizedTypeReference<>() {
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
                        assertThat(memberInfo.getLoginId()).isEqualTo(setUpMemberReqDTO.getLoginId());
                        assertThat(memberInfo.getEmail()).isEqualTo(setUpMemberReqDTO.getEmail());
                        assertThat(memberInfo.getName()).isEqualTo(setUpMemberReqDTO.getName());
                        assertThat(memberInfo.getBirth()).isEqualTo(setUpMemberReqDTO.getBirth());
                        assertThat(memberInfo.getGender()).isEqualTo(setUpMemberReqDTO.getGender());
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

    @Nested
    @DisplayName("GET /api/v1/points")
    class GetMemberPoint {

        /*
         * - [ ]  포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.
         * - [ ]  `X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.
         */

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnMemberPoint_whenGetMemberPointSuccessful() {
            // given
            ResponseEntity<ApiResponse<MemberRegisterResDTO>> saved =
                    testRestTemplate.exchange("/api/v1/users", HttpMethod.POST, new HttpEntity<>(setUpMemberReqDTO),
                            new ParameterizedTypeReference<>() {
                            });
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", "test");

            // when
            ResponseEntity<ApiResponse<MemberPointResDTO>> response = testRestTemplate.exchange("/api/v1/points?memberId=" + saved.getBody().data().getId(),
                    HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().getPoint()).isNotNull()
            );
        }

        @DisplayName("`X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void returnBadRequest_whenXUserIdHeaderIsMissing() {
            // when
            ResponseEntity<ApiResponse<MemberPointResDTO>> response = testRestTemplate.exchange("/api/v1/points",
                    HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().meta()).isNotNull()
            );
        }
    }
}
