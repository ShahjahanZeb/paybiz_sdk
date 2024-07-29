package com.example.paybizsdk.entity;

public class CompletionEvent {

    private String sdkTransactionId;
    private String transactionStatus;

    public CompletionEvent(String sdkTransactionId, String transactionStatus) {
        this.sdkTransactionId = sdkTransactionId;
        this.transactionStatus = transactionStatus;
    }

    public String getSdkTransactionId() {
        return sdkTransactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }
}
