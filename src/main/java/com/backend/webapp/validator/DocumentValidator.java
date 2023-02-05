package com.backend.webapp.validator;

import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;

import static com.backend.webapp.constant.ErrorConstants.INVALID_DOCUMENT_ERROR_DESCRIPTION;
import static com.backend.webapp.constant.ErrorConstants.USER_DOES_NOT_EXIST;

public final class DocumentValidator {

    private DocumentValidator() {
    }

    public static void validateUserDocument(Users user) throws CustomError {
        if (null == user) {
            throw new CustomError(USER_DOES_NOT_EXIST);
        }
        if (null == user.getEmail() || null == user.getPasswordHash()) {
            throw new CustomError(INVALID_DOCUMENT_ERROR_DESCRIPTION);
        }
    }

}
