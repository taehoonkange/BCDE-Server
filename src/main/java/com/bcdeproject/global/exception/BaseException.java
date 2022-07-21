package com.bcdeproject.global.exception;

/**
 * 모든 커스텀 예외의 부모 클래스
 * BaseExceptionType을 반환하는 getExceptionType() 메소드
 */
public abstract class BaseException extends RuntimeException {
    public abstract BaseExceptionType getExceptionType();
}
