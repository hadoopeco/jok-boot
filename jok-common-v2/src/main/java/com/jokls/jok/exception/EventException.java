package com.jokls.jok.exception;

public class EventException extends JRESBaseException {
    private static final long serialVersionUID = 1L;
    private String threadName = Thread.currentThread().getName();

    public EventException(String errorNo, Object... errorInfo) {
        super(errorNo, (Throwable) null, errorInfo);
        StringBuffer sb = new StringBuffer();
        sb.append(this.message);
        sb.append(" - [");
        sb.append(this.threadName);
        sb.append("]");
        this.message = sb.toString();
    }

    public EventException(Throwable e, String errorNo, Object... errorInfo) {
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
