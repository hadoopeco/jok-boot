package com.jokls.jok.exception;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:15
 */
public class EventRuntimeException extends JRESBaseRuntimeException{
    private static final long serialVersionUID = 1L;
    private String threadName = Thread.currentThread().getName();

    public EventRuntimeException(String errorNo, Object... errorInfo) {
        super(errorNo, (Throwable)null, errorInfo);
        StringBuffer sb = new StringBuffer();
        sb.append(this.message);
        sb.append(" - [");
        sb.append(this.threadName);
        sb.append("]");
        this.message = sb.toString();
    }

    public EventRuntimeException(Throwable e, String errorNo, Object... errorInfo) {
        super(errorNo, e, errorInfo);
        StringBuffer sb = new StringBuffer();
        sb.append(this.message);
        sb.append(" - [");
        sb.append(this.threadName);
        sb.append("]");
        this.message = sb.toString();
    }

    public String getErrorNo() {
        return this.errorNo;
    }

    public String getErrorInfo() {
        return super.getMessage();
    }

    public String getThreadName() {
        return this.threadName;
    }
}
