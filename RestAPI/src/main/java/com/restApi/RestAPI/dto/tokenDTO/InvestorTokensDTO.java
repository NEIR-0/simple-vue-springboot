package com.restApi.RestAPI.dto.tokenDTO;

import com.restApi.RestAPI.model.auth.Users;

public class InvestorTokensDTO {
    private String hash;
    private Users userid;
    private Long tokenId;
    private String addressInvestor;
    private double holdToken;
    private double holdAfterBurn;
    private double profitBurn;

    // Getters and Setters
    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Users getUsers() {
        return userid;
    }

    public void setUsers(Users userid) {
        this.userid = userid;
    }

    public String getAddressInvestor() {
        return addressInvestor;
    }

    public void setAddressInvestor(String addressInvestor) {
        this.addressInvestor = addressInvestor;
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
