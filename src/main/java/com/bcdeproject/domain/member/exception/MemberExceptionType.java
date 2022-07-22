package com.bcdeproject.domain.member.exception;

import com.bcdeproject.global.exception.BaseExceptionType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * BaseExceptionType interface를 구현한 Member 예외 타입
 * 에러 코드, HTTP 상태, 에러 메시지 존재
 * throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME)
 */
@Getter
public enum MemberExceptionType implements BaseExceptionType {
    //== 회원가입, 로그인 시 ==//
    ALREADY_EXIST_USERNAME(600, HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    WRONG_PASSWORD(601,HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_MEMBER(602, HttpStatus.NOT_FOUND, "회원 정보가 없습니다."),
    MEMBER_UPDATE_INFO_NOT_FOUND(603, HttpStatus.BAD_REQUEST, "회원 업데이트 항목 중 적어도 하나를 입력해주세요.");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    MemberExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}