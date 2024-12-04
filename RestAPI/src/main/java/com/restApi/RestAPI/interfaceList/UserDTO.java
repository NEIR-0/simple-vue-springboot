package com.restApi.RestAPI.interfaceList;

import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private CardDTO card;
    private List<JobDTO> jobs;

    // Constructors, getters, setters

    public UserDTO(Long id, String name, CardDTO card, List<JobDTO> jobs) {
        this.id = id;
        this.name = name;
        this.card = card;
        this.jobs = jobs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public List<JobDTO> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDTO> jobs) {
        this.jobs = jobs;
    }
}
