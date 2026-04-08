package com.daitem.user_service.exception;

public class InsufficientPointException extends RuntimeException {
    public InsufficientPointException(String message) {
        super(message);
    }
}
