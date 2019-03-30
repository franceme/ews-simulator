package com.worldpay.simulator.pojo;

import com.worldpay.simulator.WalletType;

public class OutputFields {

    private String pan;
    private String token;
    private String regId;
    private boolean tokenNewlyGenerated;
    private String expDate;
    private String eci;
    private WalletType walletType;
    private String cvv;
    private String orderLVT;
    private String tokenRandomMod10;
    private String tokenWithPANLast4Mod10;
    private String tokenVault1Mod10;
    private String tokenWithPANFirst6Last4Mod10;
    private String tokenRandomMod11;
    private String tokenWithPANLast4Mod11;
    private String tokenVault1Mod11;
    private String tokenWithPANFirst6Last4Mod11;


    public String getTokenRandomMod10() {
        return tokenRandomMod10;
    }

    public void setTokenRandomMod10(String tokenRandomMod10) {
        this.tokenRandomMod10 = tokenRandomMod10;
    }

    public String getTokenWithPANLast4Mod10() {
        return tokenWithPANLast4Mod10;
    }

    public void setTokenWithPANLast4Mod10(String tokenWithPANLast4Mod10) {
        this.tokenWithPANLast4Mod10 = tokenWithPANLast4Mod10;
    }

    public String getTokenVault1Mod10() {
        return tokenVault1Mod10;
    }

    public void setTokenVault1Mod10(String tokenVault1Mod10) {
        this.tokenVault1Mod10 = tokenVault1Mod10;
    }

    public String getTokenWithPANFirst6Last4Mod10() {
        return tokenWithPANFirst6Last4Mod10;
    }

    public void setTokenWithPANFirst6Last4Mod10(String tokenWithPANFirst6Last4Mod10) {
        this.tokenWithPANFirst6Last4Mod10 = tokenWithPANFirst6Last4Mod10;
    }

    public String getTokenRandomMod11() {
        return tokenRandomMod11;
    }

    public void setTokenRandomMod11(String tokenRandomMod11) {
        this.tokenRandomMod11 = tokenRandomMod11;
    }

    public String getTokenWithPANLast4Mod11() {
        return tokenWithPANLast4Mod11;
    }

    public void setTokenWithPANLast4Mod11(String tokenWithPANLast4Mod11) {
        this.tokenWithPANLast4Mod11 = tokenWithPANLast4Mod11;
    }

    public String getTokenVault1Mod11() {
        return tokenVault1Mod11;
    }

    public void setTokenVault1Mod11(String tokenVault1Mod11) {
        this.tokenVault1Mod11 = tokenVault1Mod11;
    }

    public String getTokenWithPANFirst6Last4Mod11() {
        return tokenWithPANFirst6Last4Mod11;
    }

    public void setTokenWithPANFirst6Last4Mod11(String tokenWithPANFirst6Last4Mod11) {
        this.tokenWithPANFirst6Last4Mod11 = tokenWithPANFirst6Last4Mod11;
    }

    public String getPAN() {
        return pan;
    }

    public void setPAN(String pan) {
        this.pan = pan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public boolean getTokenNewlyGenerated() {
        return tokenNewlyGenerated;
    }

    public void setTokenNewlyGenerated(boolean tokenNewlyGenerated) {
        this.tokenNewlyGenerated = tokenNewlyGenerated;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getECI() {
        return eci;
    }

    public void setECI(String eci) {
        this.eci = eci;
    }

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public String getCVV() {
        return cvv;
    }

    public void setCVV(String cvv) {
        this.cvv = cvv;
    }

    public String getOrderLVT() {
        return orderLVT;
    }

    public void setOrderLVT(String orderLVT) {
        this.orderLVT = orderLVT;
    }
}
