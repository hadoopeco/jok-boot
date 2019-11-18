package com.jokls.jok.rpc.t2.impl.annotation;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.annotation.CloudFunctionParam;
import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.rpc.t2.definition.type.complex.JavabeanJokType;
import com.jokls.jok.rpc.t2.definition.type.complex.PropertiesJokType;
import com.jokls.jok.util.ParamNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:31
 */
public class DefPropertiesParser extends PropertiesParser {
    private final Logger logger = LoggerFactory.getLogger(DefPropertiesParser.class);
    private static final String IS_POJO_PREFIX = "class ";

    @Override
    public JokType getParamType(String typeName, TypeDefinitionContainer typeContainer) {
        typeName = this.transformType(typeName);
        JokType type = typeContainer.getType(typeName);
        if (type == null) {
            type = this.getPropertiesType(typeName, typeContainer);
        }
        return type;

    }

    @Override
    public IDataset getObjectType(Object target) {
        return null;
    }


    protected PropertiesJokType getPropertiesType(String typeName, TypeDefinitionContainer typeContainer) {
        PropertiesJokType type = (PropertiesJokType)typeContainer.getType(typeName);
        if (type == null && ClassUtils.isPresent(typeName, Thread.currentThread().getContextClassLoader())) {
            if (typeName.startsWith("java.util") || typeName.startsWith("java.lang")) {
                return null;
            }

            try {
                Class<?> clazz = Class.forName(typeName);
                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()) {
                    return null;
                }

                type = new JavabeanJokType(typeName);
                typeContainer.addType(type);
                type.setSubTypes(this.parseProperties(clazz, typeContainer));
            } catch (ClassNotFoundException e) {
                this.logger.warn("获取参数类型失败", e);
                type = null;
            }
        }

        return type;
    }

    private List<Parameter> parseProperties(Class targetClass, TypeDefinitionContainer typeContainer) {
        Map<Class, DefPropertiesParser.TypeParamInfo> typeInfos = new HashMap();
        Map<String, Parameter> paramNames = new HashMap();

        ArrayList parameters;
        for(parameters = new ArrayList(); targetClass != null && !targetClass.equals(Object.class); targetClass = targetClass.getSuperclass()) {
            DefPropertiesParser.TypeParamInfo typeInfo = typeInfos.get(targetClass);
            Map<String, String> typeParams = null;
            if (typeInfo != null) {
                typeParams = typeInfo.getTypeParams();
            } else {
                typeParams = new HashMap();
            }

            Field[] fields = targetClass.getDeclaredFields();


            for(Field field :  fields) {
                if (!this.ignore(field)) {
                    String fieldName = field.getName();
                    if (paramNames.get(fieldName) == null && isValidProperty(targetClass, fieldName)) {
                        Parameter param = this.parseField(field, typeParams, typeContainer);
                        paramNames.put(fieldName, param);
                        parameters.add(param);
                    }
                }
            }

            DefPropertiesParser.TypeParamInfo parentInfo = this.parseParentInfo(targetClass);
            if (parentInfo != null) {
                typeInfos.put(targetClass.getSuperclass(), parentInfo);
            }
        }

        return parameters;
    }

    private DefPropertiesParser.TypeParamInfo parseParentInfo(Class targetClass) {
        java.lang.reflect.Type parentType = targetClass.getGenericSuperclass();
        if (parentType instanceof ParameterizedType) {
            Class parentClass = targetClass.getSuperclass();
            ParameterizedType superParamType = (ParameterizedType)parentType;
            java.lang.reflect.Type[] args = superParamType.getActualTypeArguments();
            TypeVariable[] variables = parentClass.getTypeParameters();
            if (args.length > 0 && args.length == variables.length) {
                DefPropertiesParser.TypeParamInfo fieldInfo = new DefPropertiesParser.TypeParamInfo();
                fieldInfo.setTypeName(superParamType.toString());
                String[] typeClasses = new String[args.length];

                for(int i = 0; i < args.length; ++i) {
                    typeClasses[i] = ((Class)args[i]).getName();
                }

                String[] typeVariables = new String[variables.length];

                for(int i = 0; i < variables.length; ++i) {
                    typeVariables[i] = variables[i].getName();
                }

                fieldInfo.setTypeClasses(typeClasses);
                fieldInfo.setTypeVariables(typeVariables);
                return fieldInfo;
            }
        }

        return null;
    }

    private boolean ignore(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isTransient(modifiers);
    }

    private boolean isArrayType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    private Parameter parseField(Field field, Map<String, String> typeParams, TypeDefinitionContainer typeContainer) {
        Parameter parameter = new Parameter();
        String genericType;
        JokType type;
        if (this.isArrayType(field.getType())) {
            parameter.setArray(true);
            genericType = this.getFieldGenericType(field);
            if (typeParams.containsKey(genericType)) {
                genericType = typeParams.get(genericType);
            }

            type = this.getParamType(this.transformType(genericType), typeContainer);
            parameter.setType(type);
        } else {
            parameter.setArray(false);
            genericType = field.getType().getName();
            if (typeParams.containsKey(genericType)) {
                genericType = (String)typeParams.get(genericType);
            }

            type = this.getParamType(this.transformType(genericType), typeContainer);
            if (type == null) {
                this.logger.warn("parseProperties error: {}", field);
            }

            parameter.setType(type);
        }

        CloudFunctionParam anno = AnnotatedElementUtils.findMergedAnnotation(field, CloudFunctionParam.class);
        if (anno != null) {
            String value = anno.value();
            parameter.setTransportName(value);
        } else {
            parameter.setTransportName(ParamNameUtils.camelCase2Underscore(field.getName()));
        }

        parameter.setJavaName(field.getName());
        return parameter;
    }

    private String getFieldGenericType(Field field) {
        java.lang.reflect.Type gType = field.getGenericType();
        ParameterizedType pType = (ParameterizedType)gType;
        String actualType = pType.getActualTypeArguments()[0].toString();
        this.logger.debug("发现泛型:{}  实际类型:{}", field.getType().getName(), actualType);
        String[] strs = actualType.split(" ");
        if (strs.length == 2) {
            if (actualType.startsWith("class ")) {
                return strs[1];
            }

            ParameterizedType inParatype = (ParameterizedType)pType.getActualTypeArguments()[0];
            String inActualType = inParatype.getRawType().toString();
            String[] inStrs = inActualType.split(" ");
            if (inStrs.length == 2) {
                return inStrs[1];
            }
        }

        return actualType;
    }

    private String transformType(String className) {
        if (className == null) {
            return null;
        } else if (!className.equals("[Ljava.lang.Byte;") && !className.equals("[B")) {
            Class clz = null;

            try {
                clz = Class.forName(className);
                if (Double.class.isAssignableFrom(clz)) {
                    return "double";
                }

                if (Float.class.isAssignableFrom(clz)) {
                    return "float";
                }

                if (Timestamp.class.isAssignableFrom(clz)) {
                    return "Timestamp";
                }

                if (Short.class.isAssignableFrom(clz)) {
                    return "short";
                }

                if (Date.class.isAssignableFrom(clz)) {
                    return "Date";
                }

                if (String.class.isAssignableFrom(clz)) {
                    return "String";
                }

                if (BigDecimal.class.isAssignableFrom(clz)) {
                    return "BigDecimal";
                }

                if (Integer.class.isAssignableFrom(clz)) {
                    return "int";
                }

                if (Boolean.class.isAssignableFrom(clz)) {
                    return "boolean";
                }

                if (Byte.class.isAssignableFrom(clz)) {
                    return "byte";
                }

                if (Character.class.isAssignableFrom(clz)) {
                    return "char";
                }

                if (Map.class.isAssignableFrom(clz)) {
                    return "Map";
                }

                if (Long.class.isAssignableFrom(clz)) {
                    return "long";
                }
            } catch (ClassNotFoundException e) {
            }

            return className;
        } else {
            return "byte[]";
        }
    }

    private class TypeParamInfo {
        private String typeName;
        private String[] typeClasses;
        private String[] typeVariables;

        private TypeParamInfo() {
            this.typeClasses = new String[0];
            this.typeVariables = new String[0];
        }

        public Map<String, String> getTypeParams() {
            Map<String, String> types = new HashMap();

            for(int i = 0; i < this.typeVariables.length; ++i) {
                types.put(this.typeVariables[i], this.typeClasses[i]);
            }

            return types;
        }

        public String getTypeName() {
            return this.typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String[] getTypeClasses() {
            return this.typeClasses;
        }

        public void setTypeClasses(String[] typeClasses) {
            this.typeClasses = typeClasses;
        }

        public String[] getTypeVariables() {
            return this.typeVariables;
        }

        public void setTypeVariables(String[] typeVariables) {
            this.typeVariables = typeVariables;
        }
    }

}
