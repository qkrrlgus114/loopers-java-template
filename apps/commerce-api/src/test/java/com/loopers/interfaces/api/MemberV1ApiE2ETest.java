package com.loopers.interfaces.api;

import com.loopers.interfaces.api.member.dto.request.MemberRegisterReqDTO;
import com.loopers.interfaces.api.member.dto.response.MemberRegisterResDTO;
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
}
