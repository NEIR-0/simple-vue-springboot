package com.restApi.RestAPI.model.notification;

import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.token.Tokens;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "token_id", nullable = false)
    private Tokens token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(name = "tx_hash", nullable = false)
    private String txHash;

    @Column(name = "status")
    private String status;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

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

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Tokens getToken() {
        return token;
    }

    public void setToken(Tokens token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
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
