package com.jokls.jok.common.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:42
 */
public class BaseCommonException extends BaseException {
    private static final long serialVersionUID = -6003749635161707230L;

    public BaseCommonException(int errorCode) {
        super(errorCode + "");
    }

    public BaseCommonException(int errorCode, Throwable cause) {
        super(errorCode + "", cause);
    }

    public BaseCommonException(int errorCode, String... messages) {
        super(errorCode + "", messages);
    }

    public BaseCommonException(int errorCode, Throwable cause, String... messages) {
        super(errorCode + "", cause, messages);
    }

    public BaseCommonException(String errorCode) {
        super(errorCode);
    }

    public BaseCommonException(String errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseCommonException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseCommonException(String errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }
}
