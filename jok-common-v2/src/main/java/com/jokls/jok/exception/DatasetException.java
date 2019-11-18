package com.jokls.jok.exception;


public class DatasetException extends EventException {
    private static final long serialVersionUID = -2029701927644278477L;

    public DatasetException(String errorNo, Object... errorInfo) {
        super(errorNo, errorInfo);
    }

    public DatasetException(Throwable e, String errorNo, Object... errorInfo) {
        super(errorNo, new Object[]{e, errorInfo});
    }
}