package com.backend.webapp.model;

public class LoginResponse extends BaseResponse {

    private String signedJwtToken;

    public String getSignedJwtToken() {
        return signedJwtToken;
    }

    public void setSignedJwtToken(String signedJwtToken) {
        this.signedJwtToken = signedJwtToken;
    }

    public LoginResponse signedJwtToken(String token) {
        this.setSignedJwtToken(token);
        return this;
    }

}
