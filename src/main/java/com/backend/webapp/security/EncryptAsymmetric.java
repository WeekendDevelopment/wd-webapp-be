package com.backend.webapp.security;

import com.google.cloud.kms.v1.CryptoKeyVersionName;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.PublicKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import static com.backend.webapp.security.GCPConstants.KEY_ID;
import static com.backend.webapp.security.GCPConstants.KEY_RING_ID;
import static com.backend.webapp.security.GCPConstants.KEY_VERSION_ID;
import static com.backend.webapp.security.GCPConstants.LOCATION_ID;
import static com.backend.webapp.security.GCPConstants.PROJECT_ID;

public final class EncryptAsymmetric {

    private EncryptAsymmetric() {
        // private constructor to avoid instantiation
    }

    public static String encryptAsymmetric(String plaintext) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(), oaepParams);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] convertPemToDer(String pem) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(pem));
        String encoded = bufferedReader.lines()
                .filter(line -> !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
                .collect(Collectors.joining());
        return Base64.getDecoder().decode(encoded);
    }

    public static String getPublicKeyAsString() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(getPublicKey().getEncoded());
    }

    private static java.security.PublicKey getPublicKey()
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            CryptoKeyVersionName keyVersionName = CryptoKeyVersionName.of(PROJECT_ID, LOCATION_ID, KEY_RING_ID, KEY_ID,
                    KEY_VERSION_ID);
            PublicKey publicKey = client.getPublicKey(keyVersionName);
            byte[] derKey = convertPemToDer(publicKey.getPem());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(derKey);
            java.security.PublicKey rsaKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            return rsaKey;
        }
    }
}
