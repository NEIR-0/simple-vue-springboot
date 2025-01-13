package com.restApi.RestAPI.dto.tokenDTO;

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

    // Constructor
    public TokenDTO(Long id, String name, int tokenPrice, String symbol, int profitPersen, String status, double totalSupply, String addressToken, int totalBurn, int alreadyBurn) {
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

    public int getTotalBurn() {
        return totalBurn;
    }

    public void setTotalBurn(int totalBurn) {
        this.totalBurn = totalBurn;
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

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }
}
