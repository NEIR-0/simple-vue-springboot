package com.restApi.RestAPI.interfaceList;

public class CardDTO {
    private String cardNumber;
    private String joinDate;

    // Constructor, Getter, Setter

    public CardDTO(String cardNumber, String joinDate) {
        this.cardNumber = cardNumber;
        this.joinDate = joinDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
}
