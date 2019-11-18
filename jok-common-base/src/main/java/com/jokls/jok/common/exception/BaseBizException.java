package com.jokls.jok.common.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:41
 */
public class BaseBizException extends BaseCommonException{
    private static final long serialVersionUID = -1654620276470609163L;

    public BaseBizException(int errorCode) {
        super(errorCode);
    }

    public BaseBizException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseBizException(int errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseBizException(int errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }

    public BaseBizException(String errorCode) {
        super(errorCode);
    }

    public BaseBizException(String errorCode, String... messages) {
        super(errorCode, messages);
    }

    public BaseBizException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BaseBizException(String errorCode, Throwable cause, String... messages) {
        super(errorCode, cause, messages);
    }
}
