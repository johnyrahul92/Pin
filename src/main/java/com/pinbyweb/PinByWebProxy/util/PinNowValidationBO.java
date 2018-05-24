package com.pinbyweb.PinByWebProxy.util;

public class PinNowValidationBO {
    String channelId;
    String langId;
    String deviceId;
    String localeId;
    String apiVersion;
    String transactionDate;
    String issuerId;
    String cardNumber;
    String secureToken;
    byte[] signedBytes;

    public PinNowValidationBO() {
    }

    public byte[] getSignedBytes() {
        return this.signedBytes;
    }

    public void setSignedBytes(byte[] signedBytes) {
        this.signedBytes = signedBytes;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLangId() {
        return this.langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLocaleId() {
        return this.localeId;
    }

    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getIssuerId() {
        return this.issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecureToken() {
        return this.secureToken;
    }

    public void setSecureToken(String secureToken) {
        this.secureToken = secureToken;
    }
}

