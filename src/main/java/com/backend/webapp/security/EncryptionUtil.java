package com.backend.webapp.security;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import com.google.cloud.spring.secretmanager.SecretManagerTemplate;

public final class EncryptionUtil {

    private EncryptionUtil() {
    }

    private static KeyStore keyStore;

    public static String encryptData(SecretManagerTemplate secretManagerTemplate, String data) throws Exception {
        try {
            loadKeyStore(secretManagerTemplate);
            PublicKey publickey = keyStore.getCertificate("servicekey").getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publickey);
            return Base64.getEncoder().encodeToString(cipher.doFinal("Hello World".getBytes()));
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getPublicKeyAsString(SecretManagerTemplate secretManagerTemplate) throws KeyStoreException {
        loadKeyStore(secretManagerTemplate);
        return Base64.getEncoder().encodeToString(keyStore.getCertificate("servicekey").getPublicKey().getEncoded());
    }

    public static String decryptData(SecretManagerTemplate secretManagerTemplate, String cipherText) throws Exception {
        try {
            loadKeyStore(secretManagerTemplate);
            PrivateKey privatekey = (PrivateKey) keyStore.getKey("servicekey",
                    secretManagerTemplate.getSecretString(GCPConstants.SERVICE_KEYSTORE_JKS_PWD).toCharArray());
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privatekey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            throw e;
        }
    }

    private static void loadKeyStore(SecretManagerTemplate secretManagerTemplate) {
        try {
            if (EncryptionUtil.keyStore == null) {
                String keyStorePassword = secretManagerTemplate.getSecretString(GCPConstants.SERVICE_KEYSTORE_JKS_PWD);
                String keyStoreEncoded = secretManagerTemplate.getSecretString(GCPConstants.SERVICE_KEYSTORE_JKS);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(new ByteArrayInputStream(Base64.getDecoder().decode(keyStoreEncoded)),
                        keyStorePassword.toCharArray());
                EncryptionUtil.keyStore = keystore;
            }
        } catch (Exception e) {
            e.printStackTrace();
            EncryptionUtil.keyStore = null;
        }
    }

}
