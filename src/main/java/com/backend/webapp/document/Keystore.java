package com.backend.webapp.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Keystore {

    @Id
    private String id;

    private String name;

    private String keystore;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKeystore() {
        return keystore;
    }

}
