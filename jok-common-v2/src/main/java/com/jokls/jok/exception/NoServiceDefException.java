package com.jokls.jok.exception;

import com.jokls.jok.common.exception.BaseException;

import java.lang.reflect.Method;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:48
 */
public class NoServiceDefException extends BaseException {
    private static final long serialVersionUID = 1L;

    public NoServiceDefException(Method method) {
        super("256");
        this.putErrorProperty("method", method);
    }

    public NoServiceDefException(String serviceId, String functionId) {
        super("256");
        this.putErrorProperty("service_id", serviceId);
        this.putErrorProperty("function_id", functionId);
        this.setErrorMessage(new String[]{"No service definition, service_id[" + serviceId + "], function_id[" + functionId + "]."});
    }
}
