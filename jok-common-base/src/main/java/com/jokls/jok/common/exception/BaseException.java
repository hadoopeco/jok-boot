package com.jokls.jok.common.exception;

import com.jokls.jok.common.util.ErrorFormatter;
import com.jokls.jok.common.util.StringUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:42
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -6853310712844466349L;
    private String errorCode = "-1";
    private String errorMessage = "";
    private List<String> errorPropNames = new ArrayList();
    private Map<String, Object> errorProperties = new HashMap();

    public BaseException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String... messages) {
        this.errorCode = errorCode;
        this.errorMessage = ErrorFormatter.getInstance().format(errorCode, messages);
    }

    public BaseException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, Throwable cause, String... messages) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = ErrorFormatter.getInstance().format(errorCode, messages);
    }

    public void setErrorMessage(String... messages) {
        this.errorMessage = ErrorFormatter.getInstance().format(this.errorCode, messages);
    }

    public void setErrorMessageNoFormat(String messages) {
        this.errorMessage = messages;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getErrorPropNames() {
        return this.errorPropNames;
    }

    public void putErrorProperty(String name, Object prop) {
        if (name != null) {
            this.errorPropNames.add(name);
            this.errorProperties.put(name, prop);
        }

    }

    public void putErrorProperty(Map<String, Object> errorProperties) {
        if (errorProperties != null) {
            Iterator it = errorProperties.entrySet().iterator();

            while(it.hasNext()) {
                Entry<String, Object> entry = (Entry)it.next();
                this.putErrorProperty(entry.getKey(), entry.getValue());
            }
        }

    }

    public Map<String, Object> getErrorProperties() {
        return this.errorProperties;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder("[" + this.errorCode + "]");
        if (!StringUtils.isEmpty(this.errorMessage)) {
            sb.append(this.errorMessage);
        } else {
            int errorPropSize = this.errorPropNames.size();
            if (errorPropSize > 0) {
                for(int i = 0; i < errorPropSize; ++i) {
                    String propName = (String)this.errorPropNames.get(i);
                    Object propValue = this.errorProperties.get(propName);
                    if (i == 0) {
                        sb.append(propName + "=" + propValue);
                    } else {
                        sb.append(", " + propName + "=" + propValue);
                    }
                }
            } else {
                sb.append(super.getMessage());
            }
        }

        return sb.toString();
    }
}
