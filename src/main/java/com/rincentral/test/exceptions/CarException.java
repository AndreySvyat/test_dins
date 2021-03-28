package com.rincentral.test.exceptions;

import lombok.Getter;

@Getter
public class CarException extends RuntimeException {

    public CarException(ExceptionConstants exceptionConstants) {
        super(exceptionConstants.getMessageTemplate());
        this.exceptionConstants = exceptionConstants;
    }

    public CarException(String message, ExceptionConstants exceptionConstants) {
        super(message);
        this.exceptionConstants = exceptionConstants;
    }

    private final ExceptionConstants exceptionConstants;
}
