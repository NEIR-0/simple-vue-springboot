package com.restApi.RestAPI.model.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email format is invalid")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    private String password;

    // constructor
    public Users() {
    }

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getter & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
