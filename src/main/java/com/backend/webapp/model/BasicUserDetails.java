package com.backend.webapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class BasicUserDetails {

    @NotBlank(message = "Required field - fullName is missing")
    private String fullName;

    @NotBlank(message = "Required field - email is missing")
    @Email
    private String email;

    @NotBlank(message = "Required field - passwordHash is missing")
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
