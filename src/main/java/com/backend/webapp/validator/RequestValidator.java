package com.backend.webapp.validator;

import com.backend.webapp.exception.CustomError;
import com.backend.webapp.model.User;
import org.apache.commons.lang3.StringUtils;

import static com.backend.webapp.constant.ErrorConstants.INVALID_PATCH_PARAMETERS;

public final class RequestValidator {

    private RequestValidator() {
    }

    public static void validatePatchUserRequest(User userUpdate) throws CustomError {
        if (!StringUtils.isAllBlank(userUpdate.getEmail(), userUpdate.getPasswordHash(), userUpdate.getRole())) {
            throw new CustomError(INVALID_PATCH_PARAMETERS);
        }
    }

}
