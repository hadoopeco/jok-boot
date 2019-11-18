package com.jokls.jok.rpc.t2.impl;

import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.rpc.t2.impl.annotation.PropertiesParser;
import com.jokls.jok.util.ParamNameUtils;
import org.springframework.util.StringUtils;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 10:45
 */
public class TypeObjectParser {
    private static TypeDefinitionContainer typeContainer;
    private static PropertiesParser defPropertiesParser;

    public TypeObjectParser() {
    }

    public Parameter getParameter(Object target, String javaName, boolean isArrary) {
        if (null == target) {
            return null;
        } else {
            Class<?> targetClz = target.getClass();
            String targetTypeName = targetClz.getName();
            Parameter parameter = new Parameter();
            parameter.setTransportName(ParamNameUtils.camelCase2Underscore(javaName));
            parameter.setJavaName(javaName);
            parameter.setArray(isArrary);
            JokType type = this.getParamType(targetTypeName);
            parameter.setType(type);
            return parameter;
        }
    }

    public Parameter getParameter(String typeName, String javaName, boolean isArrary) {
        if (!StringUtils.isEmpty(typeName) && !StringUtils.isEmpty(javaName)) {
            Parameter parameter = new Parameter();
            parameter.setTransportName(ParamNameUtils.camelCase2Underscore(javaName));
            parameter.setJavaName(javaName);
            parameter.setArray(isArrary);
            JokType type = this.getParamType(typeName);
            parameter.setType(type);
            return parameter;
        } else {
            return null;
        }
    }

    public JokType getParamType(String typeName) {
        JokType type = typeContainer.getType(typeName);
        if (type == null) {
            type = defPropertiesParser.getParamType(typeName, typeContainer);
        }

        return type;
    }

    public void setTypeContainer(TypeDefinitionContainer typeDefinitionContainer) {
        typeContainer = typeDefinitionContainer;
    }

    public void setPropertiesParser(PropertiesParser propertiesParser) {
        defPropertiesParser = propertiesParser;
    }
}
