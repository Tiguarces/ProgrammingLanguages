package com.Tiguarces.ProgrammingLanguages.controller;

import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public final class ControllerConstants {
    private ControllerConstants() { }

    public static final ResponseEntity<?> NOT_FOUND_RESPONSE = new ResponseEntity<>(NOT_FOUND);
    public static final ResponseEntity<?> INTERNAL_SERVER_ERROR_RESPONSE = new ResponseEntity<>(INTERNAL_SERVER_ERROR);
}
