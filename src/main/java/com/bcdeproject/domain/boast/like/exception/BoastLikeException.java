package com.bcdeproject.domain.boast.like.exception;

import com.bcdeproject.global.exception.BaseException;
import com.bcdeproject.global.exception.BaseExceptionType;

public class BoastLikeException extends BaseException {

    private BaseExceptionType baseExceptionType;

    public BoastLikeException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
