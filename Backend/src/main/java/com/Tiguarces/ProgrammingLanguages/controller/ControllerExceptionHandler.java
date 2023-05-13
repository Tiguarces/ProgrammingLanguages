package com.Tiguarces.ProgrammingLanguages.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.Tiguarces.ProgrammingLanguages.controller.ControllerConstants.INTERNAL_SERVER_ERROR_RESPONSE;

@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAnyException(final Exception exception) {
        log.error(exception.getLocalizedMessage());
        return INTERNAL_SERVER_ERROR_RESPONSE;
    }
}
