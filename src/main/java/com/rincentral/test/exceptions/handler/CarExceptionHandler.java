package com.rincentral.test.exceptions.handler;

import com.rincentral.test.exceptions.CarException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CarExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<String> handleException(CarException carException){
        return new ResponseEntity<>(carException.getMessage(), carException.getExceptionData().getHttpStatus());
    }
}
