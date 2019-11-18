package com.jokls.jok.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:46
 */
public class NoSuchTypeException extends RuntimeException {
    private static final long serialVersionUID = 51302197398660377L;

    public NoSuchTypeException(String message) {
        super(message);
    }

    public NoSuchTypeException(String message, Exception e) {
        super(message, e);
    }
}
