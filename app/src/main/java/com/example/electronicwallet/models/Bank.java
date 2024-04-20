package com.example.electronicwallet.models;

public class Bank {
    String bankId;
    String bankName;
    String urlLogo;
    String deepLink;

    public Bank() {
    }

    public Bank(String bankId, String bankName, String urlLogo, String deepLink) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.urlLogo = urlLogo;
        this.deepLink = deepLink;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }
}
