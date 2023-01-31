package com.backend.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.backend.webapp.util.PropertiesLoader;

import static com.backend.webapp.constant.ApplicationConstants.ALLOWED_METHODS;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private PropertiesLoader propertiesLoader;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods(ALLOWED_METHODS).allowedOrigins(propertiesLoader.getOrigin())
                .allowedHeaders("*");
    }

}