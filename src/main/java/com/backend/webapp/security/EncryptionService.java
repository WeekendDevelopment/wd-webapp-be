package com.backend.webapp.security;

import com.backend.webapp.document.Keystore;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.repository.KeystoreRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EncryptionService {

    private static final Logger logger = LogManager.getLogger(EncryptionService.class);

    private static final Map<String, KeyStore> keyStoreCache = new ConcurrentHashMap<>();

    @Autowired
    KeystoreRepository keystoreRepository;

    @Value("${keystore.name}")
    private String name;

    public String encryptData(String data) throws Exception {
        try {
            PublicKey publickey = this.getKeyStore().getCertificate("key").getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publickey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            logger.error("Exception occurred while encrypting data", e);
            throw e;
        }
    }

    public String getPublicKeyAsString() throws Exception {
        return Base64.getEncoder().encodeToString(this.getKeyStore().getCertificate("key").getPublicKey().getEncoded());
    }

    public String decryptData(String cipherText) throws Exception {
        try {
            PrivateKey privatekey = (PrivateKey) this.getKeyStore().getKey("key",
                    System.getenv("KEYSTORE_PWD").toCharArray());
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privatekey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            logger.error("Exception occurred while decrypting data", e);
            throw e;
        }
    }

    private KeyStore getKeyStore() throws Exception {
        try {
            if (keyStoreCache.containsKey("keyStore")) {
                return keyStoreCache.get("keyStore");
            }
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(fetchKeyStore(), System.getenv("KEYSTORE_PWD").toCharArray());
            keyStoreCache.put("keyStore", keystore);
            return keystore;
        } catch (Exception e) {
            logger.error("Exception occurred while loading keystore", e);
            throw e;
        }
    }

    private InputStream fetchKeyStore() throws CustomError {
        List<Keystore> keyStoreList = keystoreRepository.findByName(name);
        if (CollectionUtils.isNotEmpty(keyStoreList)) {
            return new ByteArrayInputStream(Base64.getDecoder().decode(keyStoreList.get(0).getKeystore()));
        } else {
            throw new CustomError("Unable to retrieve keystore");
        }
    }

}
