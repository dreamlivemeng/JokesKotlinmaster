package com.dreamlive.jokes.retrofit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author 2017/9/12 14:08 / mengwei
 */
public class BasicObjectMapper extends ObjectMapper {
    private String format = "yyyy-MM-dd";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BasicObjectMapper() {
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });
        setDateFormat(new SimpleDateFormat(format));
        // 忽略多余属性
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许单引号
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许key不带引号
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许控制字符
        this.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        this.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
