package com.restApi.RestAPI.dto.tokenDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class TokenDTO {
    private String name;
    private int tokenPrice;
    private String symbol;
    private int profitPersen;
    private String status;
    private double totalSupply;
    private String addressToken;
    private Long id;
    private int totalBurn;
    private int alreadyBurn;
    private double amountPerBurning;
    private double payPerBurn;
    private boolean isWithdraw;

    // Constructor
    public TokenDTO(Long id, String name, int tokenPrice, String symbol, int profitPersen, String status, double totalSupply, String addressToken, int totalBurn, int alreadyBurn, double amountPerBurning, double payPerBurn, boolean isWithdraw) {
        this.id = id;
        this.name = name;
        this.tokenPrice = tokenPrice;
        this.totalBurn = totalBurn;
        this.symbol = symbol;
        this.profitPersen = profitPersen;
        this.status = status;
        this.totalSupply = totalSupply;
        this.addressToken = addressToken;
        this.alreadyBurn = alreadyBurn;
        this.amountPerBurning = amountPerBurning;
        this.payPerBurn = payPerBurn;
        this.isWithdraw = isWithdraw;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
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

    public int getTotalBurn() {
        return totalBurn;
    }

    public void setTotalBurn(int totalBurn) {
        this.totalBurn = totalBurn;
    }

    public double getPayPerBurn() {
        return payPerBurn;
    }

    public void setPayPerBurn(double payPerBurn) {
        this.payPerBurn = payPerBurn;
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

    public int getAlreadyBurn() {
        return alreadyBurn;
    }

    public void setAlreadyBurn(int alreadyBurn) {
        this.alreadyBurn = alreadyBurn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public double getAmountPerBurning() {
        return amountPerBurning;
    }

    public void setAmountPerBurning(double amountPerBurning) {
        this.amountPerBurning = amountPerBurning;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }
}
