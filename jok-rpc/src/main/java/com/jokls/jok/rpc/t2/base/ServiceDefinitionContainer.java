package com.jokls.jok.rpc.t2.base;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:25
 */
public interface ServiceDefinitionContainer {
    ServiceDefinition getServiceDefinition(Method method);

    ServiceDefinition getServiceDefinition(String serviceId, String functionId);

    void addDefinition(ServiceDefinition def);

    void addDefinitions(List<ServiceDefinition> defs);

    /** @deprecated */
    @Deprecated
    Collection<ServiceDefinition> getAllDefinitions();

    boolean IsValidateObject();
}
