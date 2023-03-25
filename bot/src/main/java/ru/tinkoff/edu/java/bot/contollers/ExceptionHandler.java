package ru.tinkoff.edu.java.bot.contollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;

import java.security.InvalidParameterException;
import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestParameters(InvalidParameterException ex) {
        String[] stacktrace = Arrays.stream(ex.getStackTrace()).map(Object::toString).toArray(String[]::new);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), ex.getClass().getName(),
                        ex.getMessage(),
                        stacktrace));
    }
}
