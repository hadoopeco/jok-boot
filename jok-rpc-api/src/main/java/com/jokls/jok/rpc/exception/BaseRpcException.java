package com.jokls.jok.rpc.exception;

import com.jokls.jok.common.exception.BaseCommonException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 15:12
 */
public class BaseRpcException extends BaseCommonException {
    private static final long serialVersionUID = -3108168817513272513L;

    public BaseRpcException(int errorCode) {
        super(errorCode);
    }

    public BaseRpcException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseRpcException(int errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseRpcException(int errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }

    public BaseRpcException(String errorCode) {
        super(errorCode);
    }

    public BaseRpcException(String errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseRpcException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseRpcException(String errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }
}
