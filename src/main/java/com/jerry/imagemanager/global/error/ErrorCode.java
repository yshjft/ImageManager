package com.jerry.imagemanager.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "E500_SERVER", "서버 문제 발생하였습니다."),
    BAD_REQUEST(400, "E400", "잘못된 입력입니다."),
    FILE_CONVERTING_FAIL(400, "E400_FILE_CONVERTING_FAIL", "MultipartFile -> File 전환 실패"),
    INVALID_IMAGE_COUNT(400, "E400_INVALID_FILE_COUNT", "1 <= 사진 개수 <= 3"),
    INVALID_FILE_EXTENSION(400, "E400_INVALID_FILE_EXTENSION", "허용되지 않는 파일 타입입니다."),
    MAX_UPLOAD_SIZE_EXCEEDED(400, "E400_MAX_UPLOAD_SIZE_EXCEEDED", "최대 파일 크기(5MB)보다 큰 파일입니다."),
    NOT_FOUND(404, "E404", "해당 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "E405", "지원하지 않는 HTTP 메소드 입니다.")
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
