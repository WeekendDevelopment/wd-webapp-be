package com.backend.webapp.security;

import com.google.cloud.kms.v1.AsymmetricDecryptResponse;
import com.google.cloud.kms.v1.CryptoKeyVersionName;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.Base64;

import static com.backend.webapp.security.GCPConstants.KEY_ID;
import static com.backend.webapp.security.GCPConstants.KEY_RING_ID;
import static com.backend.webapp.security.GCPConstants.KEY_VERSION_ID;
import static com.backend.webapp.security.GCPConstants.LOCATION_ID;
import static com.backend.webapp.security.GCPConstants.PROJECT_ID;

public final class DecryptAsymmetric {

    private DecryptAsymmetric() {
        // private constructor to avoid instantiation
    }

    public static String decryptAsymmetric(String cipherTextEncoded) throws IOException {
        byte[] ciphertext = Base64.getDecoder().decode(cipherTextEncoded);
        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            CryptoKeyVersionName keyVersionName = CryptoKeyVersionName.of(PROJECT_ID, LOCATION_ID, KEY_RING_ID, KEY_ID,
                    KEY_VERSION_ID);
            AsymmetricDecryptResponse response = client.asymmetricDecrypt(keyVersionName,
                    ByteString.copyFrom(ciphertext));
            return response.getPlaintext().toStringUtf8();
        }
    }
}
