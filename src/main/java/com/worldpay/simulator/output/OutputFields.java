package com.worldpay.simulator.output;

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
