package com.jokls.jok.exception;

import com.jokls.jok.common.util.ErrorFormatter;

public class JRESBaseException extends Exception implements IJRESBaseErrorMessage {
    private static final long serialVersionUID = 1L;
    protected String errorNo;
    protected String message;

    public JRESBaseException() {
    }

    public JRESBaseException(String message) {
        super(message);
        this.errorNo = "-1";
        this.message = message;
    }

    public JRESBaseException(Throwable cause, String message) {
        super(cause);
        this.errorNo = "-1";
        this.message = ErrorFormatter.getInstance().format(this.errorNo, new Object[]{message});
    }

    public JRESBaseException(Throwable cause) {
        super(cause);
        this.errorNo = "-1";
        this.message = cause.getMessage();
    }

    public JRESBaseException(String errorNo, String message) {
        super(message);
        this.errorNo = errorNo;
        this.message = ErrorFormatter.getInstance().format(errorNo, new Object[]{message});
    }

    public JRESBaseException(String errorNo, String message, Throwable cause) {
        super(cause);
        this.errorNo = errorNo;
        this.message = ErrorFormatter.getInstance().format(errorNo, new Object[]{message});
    }

    public JRESBaseException(String errorNo, Throwable cause, Object... messages) {
        super(cause);
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
        return this.getMessage();
    }

    public void setErrorNo(String errorNo) {
        this.errorNo = errorNo;
    }

    public void setErrorMessage(String message) {
        this.message = message;
    }
}