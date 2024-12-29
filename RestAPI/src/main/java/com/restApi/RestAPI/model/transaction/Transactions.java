package com.restApi.RestAPI.model.transaction;

import com.restApi.RestAPI.model.auth.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private Float price;

    private String hash;

    private String status;

    private String address_signed;

    private String address_receiver;

    private String code_subscription;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Constructor
    public Transactions() {
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode_Subscription() {
        return code_subscription;
    }

    public void setCode_Subscription(String code_subscription) {
        this.code_subscription = code_subscription;
    }

    public String getAddress_signed() {
        return address_signed;
    }

    public void setAddress_signed(String address_signed) {
        this.address_signed = address_signed;
    }

    public String getAddress_Receiver() {
        return address_receiver;
    }

    public void setAddress_Receiver(String address_receiver) {
        this.address_receiver = address_receiver;
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
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}