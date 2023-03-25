package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import ru.tinkoff.edu.java.scrapper.dto.response.ApiErrorResponse;

import java.security.InvalidParameterException;
import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestParameters(InvalidParameterException ex) {
        String[] stacktrace = Arrays.stream(ex.getStackTrace()).map(Object::toString).toArray(String[]::new);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), ex.getClass().getName(),
                        ex.getMessage(),
                        stacktrace));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex) {
        String[] stacktrace = Arrays.stream(ex.getStackTrace()).map(Object::toString).toArray(String[]::new);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), ex.getClass().getName(),
                        ex.getMessage(),
                        stacktrace));
    }
}
