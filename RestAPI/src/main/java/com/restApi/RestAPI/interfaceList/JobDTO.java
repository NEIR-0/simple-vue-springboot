package com.restApi.RestAPI.interfaceList;

public class JobDTO {
    private Long id;
    private String jobTitle;
    private Double salary;

    // Constructors, getters, setters

    public JobDTO(Long id, String jobTitle, Double salary) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
