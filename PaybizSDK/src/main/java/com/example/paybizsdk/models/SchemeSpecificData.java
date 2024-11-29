package com.example.paybizsdk.models;

public class SchemeSpecificData {

    private VisaSpecificData visaSpecificData;

    public VisaSpecificData getVisaSpecificData() {
        return visaSpecificData;
    }

    public void setVisaSpecificData(VisaSpecificData visaSpecificData) {
        this.visaSpecificData = visaSpecificData;
    }

    public static class VisaSpecificData {
            private String merchantVerificationValue;
            private String marketSpecificIndicator;
            private String merchantIdentifier;
            private String commercialCardTypeRequest;
            private String authorizationCharacteristicsIndicator;

        public String getMerchantVerificationValue() {
            return merchantVerificationValue;
        }

        public void setMerchantVerificationValue(String merchantVerificationValue) {
            this.merchantVerificationValue = merchantVerificationValue;
        }

        public String getMarketSpecificIndicator() {
            return marketSpecificIndicator;
        }

        public void setMarketSpecificIndicator(String marketSpecificIndicator) {
            this.marketSpecificIndicator = marketSpecificIndicator;
        }

        public String getMerchantIdentifier() {
            return merchantIdentifier;
        }

        public void setMerchantIdentifier(String merchantIdentifier) {
            this.merchantIdentifier = merchantIdentifier;
        }

        public String getCommercialCardTypeRequest() {
            return commercialCardTypeRequest;
        }

        public void setCommercialCardTypeRequest(String commercialCardTypeRequest) {
            this.commercialCardTypeRequest = commercialCardTypeRequest;
        }

        public String getAuthorizationCharacteristicsIndicator() {
            return authorizationCharacteristicsIndicator;
        }

        public void setAuthorizationCharacteristicsIndicator(String authorizationCharacteristicsIndicator) {
            this.authorizationCharacteristicsIndicator = authorizationCharacteristicsIndicator;
        }
    }

}
