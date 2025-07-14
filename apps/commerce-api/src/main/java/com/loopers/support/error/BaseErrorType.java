package com.loopers.support.error;

import org.springframework.http.HttpStatus;

public interface BaseErrorType {

    String getMessage();

    HttpStatus getStatus();

    String getCode();
}
