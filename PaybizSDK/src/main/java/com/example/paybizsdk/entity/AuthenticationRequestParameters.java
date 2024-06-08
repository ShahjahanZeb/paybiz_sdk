package com.example.paybizsdk.entity;

public class AuthenticationRequestParameters {

    private String deviceData;
    private String SDKTransactionID;
    private String SDKAppID;
    private String SDKReferenceNumber;
    private String SDKEphemeralPublicKey;
    private String messageVersion;

    public AuthenticationRequestParameters(String deviceData, String SDKTransactionID, String SDKAppID, String SDKReferenceNumber, String SDKEphemeralPublicKey, String messageVersion) {
        this.deviceData = deviceData;
        this.SDKTransactionID = SDKTransactionID;
        this.SDKAppID = SDKAppID;
        this.SDKReferenceNumber = SDKReferenceNumber;
        this.SDKEphemeralPublicKey = SDKEphemeralPublicKey;
        this.messageVersion = messageVersion;
    }

    public String getDeviceData() {
        return deviceData;
    }

    public String getSDKTransactionID() {
        return SDKTransactionID;
    }

    public String getSDKAppID() {
        return SDKAppID;
    }

    public String getSDKReferenceNumber() {
        return SDKReferenceNumber;
    }

    public String getSDKEphemeralPublicKey() {
        return SDKEphemeralPublicKey;
    }

    public String getMessageVersion() {
        return messageVersion;
    }
}
