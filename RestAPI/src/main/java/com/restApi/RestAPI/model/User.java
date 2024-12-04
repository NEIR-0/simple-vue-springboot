package com.restApi.RestAPI.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // One user can have many jobs
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Jobs> jobs;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Card card;

    // Getters and Setters
    public User() {}

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

    public List<Jobs> getJobs() {
        return jobs;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setJobs(List<Jobs> jobs) {
        this.jobs = jobs;
    }
}
