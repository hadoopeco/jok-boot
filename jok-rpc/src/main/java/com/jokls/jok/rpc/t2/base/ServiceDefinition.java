package com.jokls.jok.rpc.t2.base;

import java.lang.reflect.Method;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 18:04
 */
public class ServiceDefinition {
    protected String serviceId;
    protected String functionId;
    protected String[] functionIds;
    protected String type;
    protected boolean async = false;
    protected String desc;


    protected Parameter request;
    protected Parameter response;
    protected Object serviceInstance;
    protected Method serviceMethod;
    protected String targetServiceUniqueName;

    public ServiceDefinition() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("functionId: " + this.functionId);
        sb.append(", serviceId: " + this.serviceId);
        sb.append(", method: " + this.serviceMethod);
        sb.append(", request: " + this.request);
        sb.append(", response: " + this.response);
        sb.append("}");
        return sb.toString();
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getFunctionId() {
        return this.functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String[] getFunctionIds() {
        return this.functionIds;
    }

    public void setFunctionIds(String[] functionIds) {
        this.functionIds = functionIds;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Parameter getRequest() {
        return this.request;
    }

    public void setRequest(Parameter request) {
        this.request = request;
    }

    public Parameter getResponse() {
        return this.response;
    }

    public void setResponse(Parameter response) {
        this.response = response;
    }

    public Object getServiceInstance() {
        return this.serviceInstance;
    }

    public void setServiceInstance(Object serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public Method getServiceMethod() {
        return this.serviceMethod;
    }

    public void setServiceMethod(Method serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetServiceUniqueName() {
        return this.targetServiceUniqueName;
    }

    public void setTargetServiceUniqueName(String targetServiceUniqueName) {
        this.targetServiceUniqueName = targetServiceUniqueName;
    }

    public boolean isAsync() {
        return this.async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
