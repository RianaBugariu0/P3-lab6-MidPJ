package com.example.exceptions;

public class PersistenceException extends Exception {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
} 