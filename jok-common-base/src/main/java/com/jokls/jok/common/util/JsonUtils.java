package com.jokls.jok.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:49
 */
public class JsonUtils {
    private static Object lock = new Object();
    private static ObjectMapper mapper = null;

    public JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            synchronized(lock) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                }
            }
        }

        return mapper;
    }

    public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return getObjectMapper().getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }
}
