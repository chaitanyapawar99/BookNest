package com.cdac.custom_exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }
}
