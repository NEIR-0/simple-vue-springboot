package com.restApi.RestAPI.dto;

import java.util.Date;

public class NotificationDTO {
    private Long id;
    private String status;
    private String tokenName;
    private String tokenStatus;
    private Date createdAt;

    public NotificationDTO(Long id, String status, String tokenName, String tokenStatus, Date createdAt) {
        this.id = id;
        this.status = status;
        this.tokenName = tokenName;
        this.tokenStatus = tokenStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getTokenStatus() {
        return tokenStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}