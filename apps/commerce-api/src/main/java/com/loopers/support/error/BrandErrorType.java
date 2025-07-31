package com.loopers.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BrandErrorType implements BaseErrorType {

    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, HttpStatus.URI_TOO_LONG.getReasonPhrase(), "브랜드를 찾을 수 없습니다."),
    FAIL_SAVED_BRAND(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "브랜드 저장에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
