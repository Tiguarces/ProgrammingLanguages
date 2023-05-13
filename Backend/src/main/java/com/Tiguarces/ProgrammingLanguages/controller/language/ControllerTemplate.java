package com.Tiguarces.ProgrammingLanguages.controller.language;

import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
interface ControllerTemplate {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getLanguageDetails(String languageName);
}
