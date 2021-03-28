package com.rincentral.test.exceptions;

import lombok.Getter;

@Getter
public class CarException extends RuntimeException {

    public CarException(ExceptionData exceptionData) {
        super(exceptionData.getMessageTemplate());
        this.exceptionData = exceptionData;
    }

    public CarException(String message, ExceptionData exceptionData) {
        super(message);
        this.exceptionData = exceptionData;
    }

    private final ExceptionData exceptionData;
}
