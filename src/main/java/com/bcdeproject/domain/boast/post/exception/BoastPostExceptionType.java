package com.bcdeproject.domain.boast.post.exception;

import com.bcdeproject.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum BoastPostExceptionType implements BaseExceptionType {

    POST_NOT_FOUND(700, HttpStatus.NOT_FOUND, "찾는 게시물이 없습니다"),
    NOT_AUTHORITY_UPDATE_POST(701, HttpStatus.FORBIDDEN, "게시물을 업데이트할 권한이 없습니다."),
    NOT_AUTHORITY_DELETE_POST(702, HttpStatus.FORBIDDEN, "게시물을 삭제할 권한이 없습니다."),
    UPDATE_POST_TITLE_NOT_FOUND(703, HttpStatus.BAD_REQUEST, "게시물 업데이트 제목을 입력해주세요."),
    UPDATE_POST_CONTENT_NOT_FOUND(704, HttpStatus.BAD_REQUEST, "게시물 업데이트 내용을 입력해주세요."),
    UPDATE_POST_HASHTAG_NOT_FOUND(705, HttpStatus.BAD_REQUEST, "게시물 업데이트 해시태그를 입력해주세요.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    BoastPostExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
