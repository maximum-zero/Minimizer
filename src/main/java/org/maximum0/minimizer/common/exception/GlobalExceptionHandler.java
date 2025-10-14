package org.maximum0.minimizer.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.maximum0.minimizer.common.response.Response;
import org.maximum0.minimizer.url.domain.exception.UrlAccessExpiredException;
import org.maximum0.minimizer.url.domain.exception.UrlNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public <T> ResponseEntity<Response<T>> handleUrlNotFoundException(UrlNotFoundException ex, HttpServletRequest request) {
        log.warn("⚠️ [UrlNotFoundException] {} {} (Params: {}) -> Details: {}", request.getMethod(), request.getRequestURI(), getRequestParams(request), ex.getMessage());
        return Response.fail(ErrorCode.URL_NOT_FOUND);
    }

    @ExceptionHandler(UrlAccessExpiredException.class)
    public <T> ResponseEntity<Response<T>> handleUrlAccessExpiredException(UrlAccessExpiredException ex, HttpServletRequest request) {
        log.warn("⚠️ [UrlAccessExpiredException] {} {} (Params: {}) -> Details: {}", request.getMethod(), request.getRequestURI(), getRequestParams(request), ex.getMessage());
        return Response.fail(ErrorCode.URL_EXPIRED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseEntity<Response<T>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s]: %s (입력값: %s)",
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()))
                .collect(Collectors.joining(", "));

        String clientMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(ErrorCode.INVALID_FORMAT.getMessage());

        log.warn("⚠️ [MethodArgumentNotValidException] {} {} (Params: {}) -> Details: {}\n- Field Errors: {}", request.getMethod(), request.getRequestURI(), getRequestParams(request), ex.getMessage(), fieldErrors);
        return Response.fail(ErrorCode.INVALID_FORMAT, clientMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("⛔️ [UncaughtException] {} {} (Params: {}) -> Details: {}", request.getMethod(), request.getRequestURI(), getRequestParams(request), ex.getMessage());
        return Response.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private String getRequestParams(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
