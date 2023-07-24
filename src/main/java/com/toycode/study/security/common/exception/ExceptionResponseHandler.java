package com.toycode.study.security.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 'Application Layer' 안에서 발생된 예외에 대해서 핸들링한다.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> defaultExceptionHandler(SecurityException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
            .status(ex.getError().getHttpStatus())
            .headers(new HttpHeaders())
            .body(ExceptionResponse.of(ex));
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ExceptionResponse {

        private final String message;

        private static ExceptionResponse of(SecurityException ex) {
            return new ExceptionResponse(ex.getMessage());
        }
    }
}
