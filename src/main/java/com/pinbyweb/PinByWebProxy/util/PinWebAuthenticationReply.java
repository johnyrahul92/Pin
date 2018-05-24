package com.pinbyweb.PinByWebProxy.util;

public class PinWebAuthenticationReply {
    private String securedUrl;
    private String errorCode;

    public PinWebAuthenticationReply() {
    }

    public void setSecuredUrl(String securedUrl) {
        this.securedUrl = securedUrl;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSecuredUrl() {
        return this.securedUrl;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}

