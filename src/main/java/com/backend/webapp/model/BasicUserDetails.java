package com.backend.webapp.model;

public class BasicUserDetails {

    private String fullName;
    private String email;
    private String passwordHash;
    private String role;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String userId) {
        this.fullName = userId;
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
