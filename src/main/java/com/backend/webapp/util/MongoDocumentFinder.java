package com.backend.webapp.util;

import com.backend.webapp.exception.CustomError;
import com.backend.webapp.document.Users;
import com.backend.webapp.validator.DocumentValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.backend.webapp.constant.ErrorConstants.DOCUMENT_NOT_FOUNT_ERROR_DESCRIPTION;

public final class MongoDocumentFinder {

    private MongoDocumentFinder() {
    }

    public static <T1 extends MongoRepository<T, String>, T> T findDocumentByIdentifier(T identifier, T1 repository)
            throws CustomError {
        List<T> resultList = repository.findAll(Example.of(identifier));
        if (CollectionUtils.isEmpty(resultList)) {
            throw new CustomError(DOCUMENT_NOT_FOUNT_ERROR_DESCRIPTION);
        }
        performCustomValidations(resultList.get(0));
        return resultList.get(0);
    }

    private static <T> void performCustomValidations(T document) throws CustomError {
        // add custom validations here
        if (document instanceof Users) {
            DocumentValidator.validateUserDocument((Users) document);
        }
    }

}
