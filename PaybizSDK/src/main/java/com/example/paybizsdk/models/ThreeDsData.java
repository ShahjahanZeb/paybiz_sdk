package com.example.paybizsdk.models;

public class ThreeDsData {

    private String eci;
    private String transStatus;
    private String authenticationValue;
    private String sli;
    private String dsTransId;
    private String acsReferenceNumber;
    private String dsReferenceNumber;
    private String acsTransId;
    private String messageVersion;
    private String threedsTransDate;

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getAuthenticationValue() {
        return authenticationValue;
    }

    public void setAuthenticationValue(String authenticationValue) {
        this.authenticationValue = authenticationValue;
    }

    public String getSli() {
        return sli;
    }

    public void setSli(String sli) {
        this.sli = sli;
    }

    public String getDsTransId() {
        return dsTransId;
    }

    public void setDsTransId(String dsTransId) {
        this.dsTransId = dsTransId;
    }

    public String getAcsReferenceNumber() {
        return acsReferenceNumber;
    }

    public void setAcsReferenceNumber(String acsReferenceNumber) {
        this.acsReferenceNumber = acsReferenceNumber;
    }

    public String getDsReferenceNumber() {
        return dsReferenceNumber;
    }

    public void setDsReferenceNumber(String dsReferenceNumber) {
        this.dsReferenceNumber = dsReferenceNumber;
    }

    public String getAcsTransId() {
        return acsTransId;
    }

    public void setAcsTransId(String acsTransId) {
        this.acsTransId = acsTransId;
    }

    public String getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(String messageVersion) {
        this.messageVersion = messageVersion;
    }

    public String getThreedsTransDate() {
        return threedsTransDate;
    }

    public void setThreedsTransDate(String threedsTransDate) {
        this.threedsTransDate = threedsTransDate;
    }
}
