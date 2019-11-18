package com.jokls.jok.exception;

import com.jokls.jok.common.exception.BaseException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 11:50
 */
public class ParseParamsException extends BaseException {
    private static final long serialVersionUID = -4447230261455694296L;

    public ParseParamsException(String message) {
        super(message);
        this.setErrorMessage(message);
    }

    public ParseParamsException(String message, Throwable e) {
        super("258", e);
        this.setErrorMessage(message);
    }
}
