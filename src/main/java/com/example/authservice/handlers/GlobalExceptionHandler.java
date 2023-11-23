package com.example.authservice.handlers;

import com.example.authservice.handlers.exceptions.AuthError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthError.class)
    public ResponseEntity<String> handleRuntimeException(AuthError e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
