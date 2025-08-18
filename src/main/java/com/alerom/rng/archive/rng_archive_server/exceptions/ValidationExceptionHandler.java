package com.alerom.rng.archive.rng_archive_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for API validation errors.
 * This class captures validation exceptions and returns a structured
 * error response with a 400 Bad Request status.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@ControllerAdvice
public class ValidationExceptionHandler {
    /**
     * Handles validation exceptions thrown by the @Valid annotation.
     * This method formats validation errors into a key-value map
     * where the key is the field name and the value is the error message.
     *
     * @param ex The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity containing the validation errors and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
