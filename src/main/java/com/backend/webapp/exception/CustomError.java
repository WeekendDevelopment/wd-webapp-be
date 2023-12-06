package com.backend.webapp.exception;

public final class CustomError extends Exception {

    private static final long serialVersionUID = 1L;

    private final String errorMessage;

    public CustomError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
