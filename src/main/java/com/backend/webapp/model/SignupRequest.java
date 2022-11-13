package com.backend.webapp.model;

import javax.validation.constraints.NotNull;

public class SignupRequest {

    @NotNull
    private String userId;

    @NotNull
    private String email;

    @NotNull
    private String passwordHash;
    private String role = "user";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
