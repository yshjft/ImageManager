package com.jerry.imagemanager.global.error.exception;

import com.jerry.imagemanager.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
