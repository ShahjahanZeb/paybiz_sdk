package com.example.paybizsdk.entity;

public class RuntimeErrorEvent {

    private String errorCode;
    private String errorMessage;

    public RuntimeErrorEvent(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
