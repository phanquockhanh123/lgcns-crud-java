package org.example.socialmediaspring.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JsonUtils {
    private JsonUtils() {
    }

    public static String objToString(Object prm) {
        var mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            return mapper.writeValueAsString(prm);
        } catch (JsonProcessingException e) {
            log.error("Parsing error. ", e);
        }
        return "";
    }

    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> returnType) {
        try {
            return new ObjectMapper().readValue(json, returnType);
        } catch (JsonProcessingException e) {
            log.error("Parsing error. ", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return new ObjectMapper().readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("Parsing error. ", e);
            return null;
        }
    }

    public static String wrapWithQuotesAndJoin(List<String> strings) {
        return strings.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(", "));
    }
}
