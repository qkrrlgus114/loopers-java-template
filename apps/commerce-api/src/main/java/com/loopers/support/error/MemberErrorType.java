package com.loopers.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorType implements BaseErrorType {

    FAIL_REGISTER(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "회원 가입에 실패했습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), "회원 정보를 찾을 수 없습니다."),
    MISSING_USER_ID_HEADER(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), "사용자 ID 헤더가 누락되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
