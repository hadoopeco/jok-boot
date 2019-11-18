package com.jokls.jok.db.core.exception;

import com.jokls.jok.common.exception.BaseCommonException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:45
 */
public class BaseDBException extends BaseCommonException {
    private static final long serialVersionUID = 8607574857426863801L;

    public BaseDBException(int errorCode) {
        super(errorCode);
    }

    public BaseDBException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseDBException(int errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseDBException(int errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }

    public BaseDBException(String errorCode) {
        super(errorCode);
    }

    public BaseDBException(String errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseDBException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseDBException(String errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }
}