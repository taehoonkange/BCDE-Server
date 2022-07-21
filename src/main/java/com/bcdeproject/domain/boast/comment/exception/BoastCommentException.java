package com.bcdeproject.domain.boast.comment.exception;

import com.bcdeproject.global.exception.BaseException;
import com.bcdeproject.global.exception.BaseExceptionType;

public class BoastCommentException extends BaseException {

    private BaseExceptionType baseExceptionType;


    public BoastCommentException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
