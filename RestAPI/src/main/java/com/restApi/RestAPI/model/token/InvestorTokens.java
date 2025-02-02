package com.restApi.RestAPI.model.token;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.restApi.RestAPI.model.auth.Users;
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
    @JsonBackReference
    private Tokens token;

    @Column(name = "hash", nullable = false)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userid;

    @Column(name = "address_investor", nullable = false)
    private String addressInvestor;

    @Column(name = "hold_token", nullable = false)
    private double holdToken;

    @Column(name = "hold_after_burn", nullable = false)
    private double holdAfterBurn;

    @Column(name = "profit_burn")
    private double profitBurn;

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
