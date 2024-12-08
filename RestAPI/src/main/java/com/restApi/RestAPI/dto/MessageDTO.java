package com.restApi.RestAPI.dto;

public class MessageDTO {
    private String from;
    private String content;

    // Constructor, getter, dan setter
    public MessageDTO() {
    }

    public MessageDTO(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
