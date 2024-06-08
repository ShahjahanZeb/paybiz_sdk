package com.example.paybizsdk.exceptions;

public class SDKRuntimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SDKRuntimeException(String message) {
        super(message);
    }

    public SDKRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
