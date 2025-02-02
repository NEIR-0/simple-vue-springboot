package com.restApi.RestAPI.dto.tokenDTO;

public class BuyTokenDTO {
    private int tokenId;
    private int userid;
    private String addressInvestor;
    private String hash;
    private double holdToken;
    private double holdAfterBurn;
    private double profitBurn;

    // Getters and Setters
    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getAddressInvestor() {
        return addressInvestor;
    }

    public void setAddressInvestor(String addressInvestor) {
        this.addressInvestor = addressInvestor;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public double getHoldToken() {
        return holdToken;
    }

    public void setHoldToken(double holdToken) {
        this.holdToken = holdToken;
    }

    public double getHoldAfterBurn() {
        return holdAfterBurn;
    }

    public void setHoldAfterBurn(double holdAfterBurn) {
        this.holdAfterBurn = holdAfterBurn;
    }

    public double getProfitBurn() {
        return profitBurn;
    }

    public void setProfitBurn(double profitBurn) {
        this.profitBurn = profitBurn;
    }
}
