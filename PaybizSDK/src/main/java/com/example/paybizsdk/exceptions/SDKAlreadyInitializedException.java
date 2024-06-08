package com.example.paybizsdk.exceptions;

public class SDKAlreadyInitializedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SDKAlreadyInitializedException(String message) {
        super(message);
    }

    public SDKAlreadyInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
