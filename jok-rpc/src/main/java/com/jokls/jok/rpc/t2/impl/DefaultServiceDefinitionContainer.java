package com.jokls.jok.rpc.t2.impl;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.exception.ParseServiceDefException;
import com.jokls.jok.rpc.t2.base.ServiceDefinition;
import com.jokls.jok.rpc.t2.base.ServiceDefinitionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:26
 */
public class DefaultServiceDefinitionContainer implements ServiceDefinitionContainer {
    private final static Logger logger = LoggerFactory.getLogger(DefaultServiceDefinitionContainer.class);
    protected ConcurrentHashMap<String, ServiceDefinition> serviceIdMapping = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, ServiceDefinition> functionIdMapping = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<Method, ServiceDefinition> methodMapping = new ConcurrentHashMap<>();
    private String isValidateObject = null;

    @Override
    public ServiceDefinition getServiceDefinition(Method method) {
        return method == null ? null : this.methodMapping.get(method);
    }

    @Override
    public ServiceDefinition getServiceDefinition(String serviceId, String functionId) {
        ServiceDefinition def = null;
        if(!StringUtils.isEmpty(serviceId)){
            def = this.serviceIdMapping.get(serviceId);
        }

        if(!StringUtils.isEmpty(functionId)){
            def = this.functionIdMapping.get(functionId);
        }
        return def;
    }

    @Override
    public void addDefinition(ServiceDefinition definition) {
        String serviceId = definition.getServiceId();
        String functionId = definition.getFunctionId();
        if( !StringUtils.isEmpty(serviceId)){
            if(this.serviceIdMapping.containsKey(serviceId)){
                if( !definition.getServiceMethod().equals(this.serviceIdMapping.get(serviceId).getServiceMethod())){
                    throw new ParseServiceDefException("严重异常：serviceId 存在重复,停止加载服务,请检查配置！" + definition.toString());
                }
            }
        }else{
            this.serviceIdMapping.put(serviceId,definition);
        }

        if(!StringUtils.isEmpty(functionId)){
            if(this.functionIdMapping.containsKey(functionId)){
                if(!definition.getServiceMethod().equals(this.functionIdMapping.get(functionId))){
                    throw new ParseServiceDefException("严重异常：functionId 存在重复,停止加载服务,请检查配置！" + definition.toString());
                }
            }
        }else{
            this.functionIdMapping.put(functionId, definition);
        }

        Method method = definition.getServiceMethod();
        if (method != null) {
            this.methodMapping.put(method, definition);
        }
    }

    @Override
    public void addDefinitions(List<ServiceDefinition> defs) {
        if(defs != null ){
            defs.forEach(this::addDefinition);
        }
    }

    @Override
    public Collection<ServiceDefinition> getAllDefinitions() {
            return this.methodMapping.values();
    }

    @Override
    public boolean IsValidateObject() {
        return this.getIsValidateObject().equals("true");
    }

    public String getIsValidateObject() {
        return this.isValidateObject;
    }
}
