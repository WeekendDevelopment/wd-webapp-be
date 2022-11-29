package com.backend.webapp.model;

public class BaseResponse {

    private RequestStatusEnum status;
    private String message;

    public RequestStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RequestStatusEnum status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseResponse status(RequestStatusEnum status) {
        this.setStatus(status);
        return this;
    }

    public BaseResponse message(String message) {
        this.setMessage(message);
        return this;
    }

}
