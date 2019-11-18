package com.jokls.jok.rpc.t2.definition.parameter;

import com.fasterxml.jackson.databind.JavaType;
import com.jokls.jok.rpc.t2.definition.type.JokType;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 18:06
 */
public class Parameter {
    private String transportName;
    private String javaName;
    private JokType type;
    private JavaType javaType;
    private Class<?> parameterType;
    private String format;
    private boolean isArray;
    private Map<String, String> properties = new HashMap();
    private String functionId;

    public Parameter() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("name: " + this.javaName);
        sb.append(", transportName: " + this.transportName);
        if (this.type != null) {
            sb.append(", type: " + this.type);
        } else {
            sb.append(", type: void");
        }

        sb.append(", isArray: " + this.isArray);
        if (this.properties.size() > 0) {
            sb.append(", properties: " + this.properties);
        }

        sb.append("}");
        return sb.toString();
    }

    public String getFunctionId() {
        return this.functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getTransportName() {
        return this.transportName;
    }

    public void setTransportName(String name) {
        this.transportName = name;
    }

    public String getJavaName() {
        return this.javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public JokType getType() {
        return this.type;
    }

    public void setType(JokType type) {
        this.type = type;
    }

    public JavaType getJavaType() {
        return this.javaType;
    }

    public void setJavaType(JavaType javaType) {
        this.javaType = javaType;
    }

    public Class<?> getParameterType() {
        return this.parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }

    public String getProperty(String name) {
        return (String)this.properties.get(name);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
