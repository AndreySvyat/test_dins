package com.rincentral.test.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Getter
public enum ExceptionData {
    TO_MUCH_PARAMS_PRESENTED("Set only model or brand", BAD_REQUEST),
    EMPTY_DATA("There is no data presented", NOT_FOUND),
    NO_SUCH_BRAND("Brand with id or name { %s } is not presented", NOT_FOUND),
    NO_SUCH_MODEL("Model %s is not presented", NOT_FOUND);

    private final String messageTemplate;
    private final HttpStatus httpStatus;
}
