package com.jokls.jok.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:41
 */
public class ParseServiceDefException extends RuntimeException {
    private static final long serialVersionUID = 5058596376075612535L;

    public ParseServiceDefException(String message) {
        super(message);
    }

    public ParseServiceDefException(String message, Throwable e) {
        super(message, e);
    }
}
