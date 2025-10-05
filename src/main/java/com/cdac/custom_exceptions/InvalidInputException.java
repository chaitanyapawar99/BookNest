package com.cdac.custom_exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class InvalidInputException extends ApiException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidInputException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.BAD_REQUEST);
    }
}
