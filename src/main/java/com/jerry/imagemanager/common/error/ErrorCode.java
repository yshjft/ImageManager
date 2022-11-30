package com.jerry.imagemanager.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "E500_SERVER", "서버 문제 발생하였습니다."),

    BAD_REQUEST(400, "E400", "잘 못된 입력입니다."),

    NOT_FOUND(404, "E404", "해당 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "E405", "지원하지 않는 HTTP 메소드 입니다."),
    ;


    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public boolean isSerious() {
        return status >= 500;
    }
}
