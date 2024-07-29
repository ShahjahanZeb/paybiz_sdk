package com.example.paybizsdk.entity;

public class ErrorMessage {

    private String errorCode;
    private String errorComponent;
    private String errorDescription;
    private String errorDetails;
    private String errorMessageType;
    private String messageVersionNumber;


    public ErrorMessage(String errorCode, String errorComponent, String errorDescription, String errorDetails, String errorMessageType, String messageVersionNumber) {
        this.errorCode = errorCode;
        this.errorComponent = errorComponent;
        this.errorDescription = errorDescription;
        this.errorDetails = errorDetails;
        this.errorMessageType = errorMessageType;
        this.messageVersionNumber = messageVersionNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorComponent() {
        return errorComponent;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public String getErrorMessageType() {
        return errorMessageType;
    }

    public String getMessageVersionNumber() {
        return messageVersionNumber;
    }
}
