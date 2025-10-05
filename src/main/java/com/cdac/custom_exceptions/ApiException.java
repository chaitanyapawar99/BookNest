package com.cdac.custom_exceptions;

import org.springframework.http.HttpStatus;
import java.io.Serial;

public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String errorCode; // optional app-specific code

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = null;
    }

    public ApiException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
