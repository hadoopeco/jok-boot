package com.jokls.jok.rpc.t2.impl;

import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:26
 */
public class TypeDefinitionContainerImpl implements TypeDefinitionContainer {
    private Map<String, JokType> types = new HashMap();

    public void addType(JokType type) {
        if (type != null && !StringUtils.isEmpty(type.getTypeName())) {
            this.types.put(type.getTypeName(), type);
        }
    }

    public JokType getType(String typeName) {
        JokType type = (JokType)this.types.get(typeName);
        return type;
    }
}
