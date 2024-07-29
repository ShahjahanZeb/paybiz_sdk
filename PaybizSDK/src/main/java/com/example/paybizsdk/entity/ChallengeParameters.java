package com.example.paybizsdk.entity;

public class ChallengeParameters {

    private String threeDSServerTransactionId;
    private String acsTransactionId;
    private String acsRefNumber;
    private String threeDSRequestorAppURL;


    public String getThreeDSServerTransactionId() {
        return threeDSServerTransactionId;
    }

    public void setThreeDSServerTransactionId(String threeDSServerTransactionId) {
        this.threeDSServerTransactionId = threeDSServerTransactionId;
    }

    public String getAcsTransactionId() {
        return acsTransactionId;
    }

    public void setAcsTransactionId(String acsTransactionId) {
        this.acsTransactionId = acsTransactionId;
    }

    public String getAcsRefNumber() {
        return acsRefNumber;
    }

    public void setAcsRefNumber(String acsRefNumber) {
        this.acsRefNumber = acsRefNumber;
    }

    public String getThreeDSRequestorAppURL() {
        return threeDSRequestorAppURL;
    }

    public void setThreeDSRequestorAppURL(String threeDSRequestorAppURL) {
        this.threeDSRequestorAppURL = threeDSRequestorAppURL;
    }
}
