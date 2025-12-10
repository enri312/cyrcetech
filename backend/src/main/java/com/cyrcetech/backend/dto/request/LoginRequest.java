package com.cyrcetech.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    @com.fasterxml.jackson.annotation.JsonProperty("username")
    private String username;

    @NotBlank(message = "Password is required")
    @com.fasterxml.jackson.annotation.JsonProperty("password")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
