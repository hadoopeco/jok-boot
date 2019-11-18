package com.jokls.jok.exception;

import com.jokls.jok.common.util.ErrorFormatter;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:13
 */
public class JRESBaseRuntimeException extends RuntimeException implements IJRESBaseErrorMessage{
    private static final long serialVersionUID = 1L;
    protected String errorNo;
    protected String message;

    public JRESBaseRuntimeException() {
    }

    public JRESBaseRuntimeException(String message) {
        super(message);
        this.errorNo = "-1";
        this.message = message;
    }

    public JRESBaseRuntimeException(Throwable cause) {
        super(cause);
        this.errorNo = "-1";
        this.message = cause.getMessage();
    }

    public JRESBaseRuntimeException(Throwable cause, String message) {
        super(cause);
        this.errorNo = "-1";
        this.message = ErrorFormatter.getInstance().format(this.errorNo, new Object[]{message});
    }

    public JRESBaseRuntimeException(String errorNo, String message) {
        super(message);
        this.errorNo = errorNo;
        this.message = ErrorFormatter.getInstance().format(errorNo, new Object[]{message});
    }

    public JRESBaseRuntimeException(String errorNo, String message, Throwable cause) {
        super(cause);
        this.errorNo = errorNo;
        this.message = ErrorFormatter.getInstance().format(errorNo, new Object[]{message});
    }

    public JRESBaseRuntimeException(String errorNo, Throwable cause, Object... messages) {
        this.errorNo = errorNo;
        this.message = ErrorFormatter.getInstance().format(errorNo, messages);
    }

    public String getErrorNo() {
        return this.errorNo;
    }

    public String getMessage() {
        return "[" + this.errorNo + "] " + this.message;
    }

    public String getErrorMessage() {
        return this.message;
    }

    public void setErrorNo(String errorNo) {
        this.errorNo = errorNo;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }

}
