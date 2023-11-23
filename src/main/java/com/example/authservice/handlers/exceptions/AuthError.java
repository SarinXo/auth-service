package com.example.authservice.handlers.exceptions;

public class AuthError extends RuntimeException{

    public AuthError(String message) {
        super(message);
    }

}
