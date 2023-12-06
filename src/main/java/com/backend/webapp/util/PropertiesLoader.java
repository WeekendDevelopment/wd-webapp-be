package com.backend.webapp.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "frontend")
@Configuration("PropertiesLoader")
public class PropertiesLoader {

    private String origin;

    public String getOrigin() {
        return this.origin;
    }

    public String setOrigin(String origin) {
        return this.origin = origin;
    }

}
