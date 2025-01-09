package com.restApi.RestAPI.model.token;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "investor_tokens")
public class InvestorTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "token_id", nullable = false)
    private Tokens token;

    @Column(name = "address_investor", nullable = false)
    private String addressInvestor;

    @Column(name = "hold_token", nullable = false)
    private int holdToken;

    @Column(name = "hold_after_burn", nullable = false)
    private int holdAfterBurn;

    @Column(name = "profit_burn", nullable = false)
    private int profitBurn;

    @Column(name = "is_withdraw", nullable = false)
    private boolean isWithdraw;

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

    public Tokens getToken() {
        return token;
    }

    public void setToken(Tokens token) {
        this.token = token;
    }

    public String getAddressInvestor() {
        return addressInvestor;
    }

    public void setAddressInvestor(String addressInvestor) {
        this.addressInvestor = addressInvestor;
    }

    public int getHoldToken() {
        return holdToken;
    }

    public void setHoldToken(int holdToken) {
        this.holdToken = holdToken;
    }

    public int getHoldAfterBurn() {
        return holdAfterBurn;
    }

    public void setHoldAfterBurn(int holdAfterBurn) {
        this.holdAfterBurn = holdAfterBurn;
    }

    public int getProfitBurn() {
        return profitBurn;
    }

    public void setProfitBurn(int profitBurn) {
        this.profitBurn = profitBurn;
    }

    public boolean isWithdraw() {
        return isWithdraw;
    }

    public void setWithdraw(boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
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
