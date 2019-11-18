package com.jokls.jok.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:11
 */
public class DatasetRuntimeException extends EventRuntimeException{
    private static final long serialVersionUID = -2029701927644278477L;

    public DatasetRuntimeException(String errorNo, Object... errorInfo) {
        super(errorNo, errorInfo);
    }

    public DatasetRuntimeException(Throwable e, String errorNo, Object... errorInfo) {
        super(errorNo, new Object[]{e, errorInfo});
    }
}
