package com.example.paybizsdk.models;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MadaRequest {

    private String transactionType;
    private String clientId;
    private String acquiringInstitutionId;
    private String terminalType;
    private String initiatedBy;
    private String tokenType;
    private String amount;
    private String merchantId;
    private String terminalId;
    private String merchantName;
    private String merchantArabicName;
    private String address;
    private String city;
    private String zipCode;
    private String regionCode;
    private String mcc;
    private String countryCode;
    private String currency;
    private String merchantReference;
    private String pan;
    private String cvv2;
    private String panExpDate;
    private String cardBrand;
    private String localDateTime;
    private Boolean threedsFlag;
    private List<String> udf;
    private ThreeDsData threeDsData;
    private SchemeSpecificData schemeSpecificData;


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAcquiringInstitutionId() {
        return acquiringInstitutionId;
    }

    public void setAcquiringInstitutionId(String acquiringInstitutionId) {
        this.acquiringInstitutionId = acquiringInstitutionId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantArabicName() {
        return merchantArabicName;
    }

    public void setMerchantArabicName(String merchantArabicName) {
        this.merchantArabicName = merchantArabicName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getPanExpDate() {
        return panExpDate;
    }

    public void setPanExpDate(String panExpDate) {
        this.panExpDate = panExpDate;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Boolean getThreedsFlag() {
        return threedsFlag;
    }

    public void setThreedsFlag(Boolean threedsFlag) {
        this.threedsFlag = threedsFlag;
    }

    public List<String> getUdf() {
        return udf;
    }

    public void setUdf(List<String> udf) {
        this.udf = udf;
    }

    public ThreeDsData getThreeDsData() {
        return threeDsData;
    }

    public void setThreeDsData(ThreeDsData threeDsData) {
        this.threeDsData = threeDsData;
    }

    public SchemeSpecificData getSchemeSpecificData() {
        return schemeSpecificData;
    }

    public void setSchemeSpecificData(SchemeSpecificData schemeSpecificData) {
        this.schemeSpecificData = schemeSpecificData;
    }
}
