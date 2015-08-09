package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class JsonObject<T> {
    private static final ObjectMapper MAPPER = buildMapper();
    private final T object;

    public JsonObject(T object) {
        this.object = object;
    }

    public JsonObject(String json) {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            this.object = MAPPER.readValue(json, MAPPER.constructType(type));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getValue() {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public T get() {
        return object;
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        return mapper;
    }
}
