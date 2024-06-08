package com.example.paybizsdk.exceptions;

public class SDKNotInitializedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SDKNotInitializedException(String message) {
        super(message);
    }

    public SDKNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
