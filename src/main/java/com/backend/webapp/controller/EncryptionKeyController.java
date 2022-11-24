package com.backend.webapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.model.BaseResponse;
import com.backend.webapp.model.RequestStatusEnum;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;

@RestController
@RequestMapping("/encryptionKey")
public class EncryptionKeyController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @SuppressWarnings("rawtypes")
    @GetMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity getPublicKey() {
        try {
            return ResponseEntity.ok(EncryptionUtil.getPublicKeyAsString(secretManagerTemplate));
        } catch (Exception e) {
            logger.error("Exception occured while getting public key from Google Cloud", e);
            return ResponseEntity.internalServerError().body(
                    new BaseResponse().status(RequestStatusEnum.FAILED).message("Failed to get publicKey from GCP"));
        }
    }

}
