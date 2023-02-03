package com.backend.webapp.controller;

import com.backend.webapp.api.EncryptionKeyApi;
import com.backend.webapp.model.EncryptionKeyResponse;
import com.backend.webapp.model.Error;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.webapp.constant.ErrorConstants.GCP_GET_PUBLIC_KEY_ERROR;

@RestController
public class EncryptionKeyController implements EncryptionKeyApi {

    private static final Logger logger = LogManager.getLogger(EncryptionKeyController.class);

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @Override
    public ResponseEntity getEncryptionKey() {
        try {
            return ResponseEntity.ok(new EncryptionKeyResponse()
                    .encryptionKey(EncryptionUtil.getPublicKeyAsString(secretManagerTemplate)));
        } catch (Exception e) {
            logger.error("Exception occured while getting public key from Google Cloud", e);
            return ResponseEntity.internalServerError().body(new Error().message(GCP_GET_PUBLIC_KEY_ERROR));
        }
    }

}
