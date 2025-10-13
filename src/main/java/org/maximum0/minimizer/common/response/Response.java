package org.maximum0.minimizer.common.response;

import org.maximum0.minimizer.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record Response<T>(
        Integer code,
        String message,
        T data
) {
    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MESSAGE = "ok";

    public static <T> ResponseEntity<Response<T>> ok(T data) {
        return ResponseEntity.ok(new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, data));
    }

    public static <T> ResponseEntity<Response<T>> ok() {
        return ResponseEntity.ok(new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, null));
    }

    public static <T> ResponseEntity<Response<T>> created(T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, data));
    }

    public static <T> ResponseEntity<Response<T>> created() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, null));
    }

    public static <T> ResponseEntity<Response<T>> fail(ErrorCode error) {
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(new Response<>(error.getCode(), error.getMessage(), null));
    }

    public static <T> ResponseEntity<Response<T>> fail(ErrorCode error, String customErrorMessage) {
        return ResponseEntity
                .status(error.getHttpStatus())
                .body(new Response<>(error.getCode(), customErrorMessage, null));
    }

}
