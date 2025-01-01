package com.restApi.RestAPI.dto;

public class MessageDTO {
    private String content;

    private Long sendTo;

    // Getters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSendTo() {
        return sendTo;
    }

    public void setSendTo(Long sendTo) {
        this.sendTo = sendTo;
    }
}
