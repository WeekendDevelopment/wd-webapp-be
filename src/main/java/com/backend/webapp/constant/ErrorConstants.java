package com.backend.webapp.constant;

public final class ErrorConstants {

    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String DUPLICATE_EMAIL_ERROR = "User already exists";
    public static final String INCORRECT_CREDENTIALS = "Email/Password incorrect";
    public static final String INVALID_REQUEST_PARAMETERS = "Invalid Request Parameters";
    public static final String GCP_GET_PUBLIC_KEY_ERROR = "Failed to get publicKey from GCP";
    public static final String DOCUMENT_NOT_FOUNT_ERROR_DESCRIPTION = "Document not found in MongoDB";
    public static final String INVALID_DOCUMENT_ERROR_DESCRIPTION = "Document retrieved from MongoDB did not pass required validations";

}
