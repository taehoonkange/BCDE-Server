package com.bcdeproject.domain.boast.like.exception;

import com.bcdeproject.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum BoastLikeExceptionType implements BaseExceptionType {

    NOT_FOUND_LIKE(900, HttpStatus.NOT_FOUND, "해당 글에 대한 좋아요 내역을 찾을 수 없습니다."),
    ALREADY_EXIST_LIKE(901, HttpStatus.BAD_REQUEST, "이미 좋아요한 글입니다.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    BoastLikeExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }


    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
