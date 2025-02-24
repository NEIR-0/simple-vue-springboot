package com.restApi.RestAPI.dto.tokenDTO;

public class UpdateTokenDTO {
    private int profitPersen;
    private String status;
    private int totalBurn;
    private double totalSupply;
    private String payPerBurn;
    private String burnTempo;

    // Getters and Setters
    public int getProfitPersen() {
        return profitPersen;
    }

    public void setProfitPersen(int profitPersen) {
        this.profitPersen = profitPersen;
    }

    public String getBurnTempo() {
        return burnTempo;
    }

    public void setBurnTempo(String burnTempo) {
        this.burnTempo = burnTempo;
    }

    public String getPayPerBurn() {
        return payPerBurn;
    }

    public void setPayPerBurn(String payPerBurn) {
        this.payPerBurn = payPerBurn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalBurn() {
        return totalBurn;
    }

    public void setTotalBurn(int totalBurn) {
        this.totalBurn = totalBurn;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }
}
