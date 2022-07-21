package com.bcdeproject.domain.boast.post.exception;

import com.bcdeproject.global.exception.BaseException;
import com.bcdeproject.global.exception.BaseExceptionType;

public class BoastPostException extends BaseException {

    private BaseExceptionType baseExceptionType;


    public BoastPostException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
