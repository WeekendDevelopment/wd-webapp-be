package com.backend.webapp.validator;

import com.backend.webapp.exception.CustomError;
import com.backend.webapp.document.Users;

import static com.backend.webapp.constant.ErrorConstants.INVALID_DOCUMENT_ERROR_DESCRIPTION;

public final class DocumentValidator {

    private DocumentValidator() {
    }

    public static void validateUserDocument(Users user) throws CustomError {
        if (null == user || null == user.getEmail() || null == user.getPasswordHash()) {
            throw new CustomError(INVALID_DOCUMENT_ERROR_DESCRIPTION);
        }
    }

}
