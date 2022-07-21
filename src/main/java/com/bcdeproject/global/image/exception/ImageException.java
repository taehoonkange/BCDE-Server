package com.bcdeproject.global.image.exception;

import com.bcdeproject.global.exception.BaseException;
import com.bcdeproject.global.exception.BaseExceptionType;

public class ImageException extends BaseException {
    private BaseExceptionType exceptionType;


    public ImageException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}