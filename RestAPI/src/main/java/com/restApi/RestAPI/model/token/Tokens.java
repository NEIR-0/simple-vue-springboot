package com.restApi.RestAPI.model.token;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restApi.RestAPI.model.auth.Users;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tokens")
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol", unique = true, nullable = false)
    private String symbol;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Users creatorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "address_creator", nullable = false)
    private String addressCreator;

    @Column(name = "total_supply", nullable = false)
    private double totalSupply;

    @Column(name = "amount_per_burning", nullable = false)
    private double amountPerBurning;

    @Column(name = "total_burn", nullable = false)
    private int totalBurn;

    @Column(name = "already_burn", nullable = false)
    private int alreadyBurn;

    @Column(name = "is_approve", nullable = false)
    @JsonProperty("isApprove")
    private Boolean isApprove;

    @Column(name = "is_withdraw", nullable = false)
    @JsonProperty("isWithdraw")
    private Boolean isWithdraw;

    @Column(name = "token_price", nullable = false)
    private String tokenPrice;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "profit_persen", nullable = false)
    private double profitPersen;

    @Column(name = "address_token", nullable = false)
    private String addressToken;

    @Column(name = "burn_tempo")
    private String burnTempo;

    @OneToMany(mappedBy = "token", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvestorTokens> investorTokens;

    @Column(name = "burn_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date burnDate;

    @Column(name = "pay_per_burn")
    private String payPerBurn;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBurnTempo() {
        return burnTempo;
    }

    public void setBurnTempo(String burnTempo) {
        this.burnTempo = burnTempo;
    }

    public Users getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(Users creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAddressCreator() {
        return addressCreator;
    }

    public void setAddressCreator(String addressCreator) {
        this.addressCreator = addressCreator;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getAmountPerBurning() {
        return amountPerBurning;
    }

    public void setAmountPerBurning(double amountPerBurning) {
        this.amountPerBurning = amountPerBurning;
    }

    public String getPayPerBurn() {
        return payPerBurn;
    }

    public void setPayPerBurn(String payPerBurn) {
        this.payPerBurn = payPerBurn;
    }

    public int getTotalBurn() {
        return totalBurn;
    }

    public void setTotalBurn(int totalBurn) {
        this.totalBurn = totalBurn;
    }

    public int getAlreadyBurn() {
        return alreadyBurn;
    }

    public void setAlreadyBurn(int alreadyBurn) {
        this.alreadyBurn = alreadyBurn;
    }

    public Boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(Boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
    }

    public Boolean isApprove() {
        return isApprove;
    }

    public void setApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }

    public String getTokenPrice() {
        return tokenPrice;
    }

    public void setTokenPrice(String tokenPrice) {
        this.tokenPrice = tokenPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddressToken() {
        return addressToken;
    }

    public void setAddressToken(String addressToken) {
        this.addressToken = addressToken;
    }

    public double getProfitPersen() {
        return profitPersen;
    }

    public void setProfitPersen(double profitPersen) {
        this.profitPersen = profitPersen;
    }

    public List<InvestorTokens> getInvestorTokens() {
        return investorTokens;
    }

    public void setInvestorTokens(List<InvestorTokens> investorTokens) {
        this.investorTokens = investorTokens;
    }

    public Date getBurnDate() {
        return burnDate;
    }

    public void setBurnDate(Date burnDate) {
        this.burnDate = burnDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }
}
