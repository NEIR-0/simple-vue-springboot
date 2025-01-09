package com.restApi.RestAPI.dto;

public class TokenDTO {
    private String name;
    private int tokenPrice;
    private String symbol;
    private int profitPersen;
    private String status;
    private int totalSupply;
    private String addressToken;

    // Constructor
    public TokenDTO(String name, int tokenPrice, String symbol, int profitPersen, String status, int totalSupply, String addressToken) {
        this.name = name;
        this.tokenPrice = tokenPrice;
        this.symbol = symbol;
        this.profitPersen = profitPersen;
        this.status = status;
        this.totalSupply = totalSupply;
        this.addressToken = addressToken;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressToken() {
        return addressToken;
    }

    public void setAddressToken(String addressToken) {
        this.addressToken = addressToken;
    }

    public int getTokenPrice() {
        return tokenPrice;
    }

    public void setTokenPrice(int tokenPrice) {
        this.tokenPrice = tokenPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getProfitPersen() {
        return profitPersen;
    }

    public void setProfitPersen(int profitPersen) {
        this.profitPersen = profitPersen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(int totalSupply) {
        this.totalSupply = totalSupply;
    }
}
