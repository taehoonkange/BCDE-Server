package com.bcdeproject.global.exception;

import org.springframework.http.HttpStatus;

/**
 * 모든 예외에서 에러코드, http 상태, 에러 메세지를 가지도록 interface 선언 후 구현
 */
public interface BaseExceptionType {
    int getErrorCode();

    HttpStatus getHttpStatus();

    String getErrorMessage();
}
