package org.maximum0.minimizer.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),
    URL_NOT_FOUND(4004, HttpStatus.NOT_FOUND, "단축 URL을 찾을 수 없습니다."),
    URL_EXPIRED(4005, HttpStatus.GONE, "단축 URL이 만료되었습니다."),
    INVALID_FORMAT(4006, HttpStatus.BAD_REQUEST, "유효하지 않은 형식입니다."),

    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
