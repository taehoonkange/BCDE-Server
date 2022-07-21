package com.bcdeproject.domain.member.exception;

import com.bcdeproject.global.exception.BaseException;
import com.bcdeproject.global.exception.BaseExceptionType;

/**
 * Member의 예외 처리 클래스
 * BaseExceptionType을 멤버 변수로 가지고, 생성자를 통해 생성하는 순간 ExceptionType 설정
 * throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME)
 */
public class MemberException extends BaseException {

    private BaseExceptionType exceptionType;

    public MemberException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
