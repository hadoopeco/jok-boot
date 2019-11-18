package com.jokls.jok.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 18:06
 */
public class SpringUtils implements ApplicationContextAware {
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    protected static ApplicationContext CONTEXT;

    public SpringUtils() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) CONTEXT.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return CONTEXT.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return CONTEXT.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeans(Class<T> requiredType) {
        return CONTEXT.getBeansOfType(requiredType);
    }

    public static boolean containsBean(String name) {
        return CONTEXT.containsBean(name);
    }
}