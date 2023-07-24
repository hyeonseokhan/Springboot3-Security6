package com.toycode.study.security.common.exception;

import lombok.Getter;

@Getter
public class SecurityException extends RuntimeException {

    private final SecurityError error;

    public SecurityException(SecurityError error) {
        super(error.getMessage());
        this.error = error;
    }

    public SecurityException(SecurityError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }
}
