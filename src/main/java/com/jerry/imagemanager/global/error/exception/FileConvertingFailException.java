package com.jerry.imagemanager.global.error.exception;

import com.jerry.imagemanager.global.error.ErrorCode;

public class FileConvertingFailException extends RuntimeException{
    private final ErrorCode errorCode;

    public FileConvertingFailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
