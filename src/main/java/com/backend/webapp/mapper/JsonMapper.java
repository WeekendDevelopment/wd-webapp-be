package com.backend.webapp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonMapper() {
    }

    public static <T> T mapJsonToObject(String json, Class<T> clazz)
            throws JsonMappingException, JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

    public static String mapObjectToJson(Object object) throws JsonMappingException, JsonProcessingException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

}
