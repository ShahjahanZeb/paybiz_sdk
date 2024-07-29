package com.example.paybizsdk.entity;

public class ProtocolErrorEvent {

    private String skdTransactionID;

    private ErrorMessage errorMessage;

    public ProtocolErrorEvent(String skdTransactionID, ErrorMessage errorMessage) {
        this.skdTransactionID = skdTransactionID;
        this.errorMessage = errorMessage;
    }

    public String getSkdTransactionID() {
        return skdTransactionID;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
