package com.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.constant.AuthException;
import com.constant.AuthDesc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

@Slf4j
public abstract class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static <T> T JsonToObj(String json, Class<T> objClazz) {
        try {
            if(StringUtils.isEmpty(json))
                return null;
            T obj = objectMapper.readValue(json, objClazz);
            return obj;
        } catch (Exception e) {
            log.error("convert data failed", e);
            throw new AuthException(AuthDesc.DATA_TRANSFORM_ERROR,null);
        }
    }

    public static  String objToJson(Object obj) {
        try{
            Objects.requireNonNull(obj);
            String json=objectMapper.writeValueAsString(obj);
            return json;
        }catch (Exception e){
            log.error("convert data failed", e);
            throw new AuthException(AuthDesc.DATA_TRANSFORM_ERROR,null);
        }
    }

    public static JsonNode loadJsonFromFile(File file){
        try {
            JsonNode jsonNode = objectMapper.readValue(file, JsonNode.class);
            return jsonNode;
        }catch (Exception e){
            log.error("load data failed", e);
            throw new AuthException(AuthDesc.DATA_TRANSFORM_ERROR,null);
        }
    }

}
