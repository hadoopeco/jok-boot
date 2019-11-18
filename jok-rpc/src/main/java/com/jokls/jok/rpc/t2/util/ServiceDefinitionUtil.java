package com.jokls.jok.rpc.t2.util;

import com.jokls.jok.common.boot.CloudConstants;
import com.jokls.jok.common.util.JsonUtils;
import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.rpc.annotation.CloudFunction;
import com.jokls.jok.rpc.annotation.CloudFunctionParam;
import com.jokls.jok.rpc.annotation.CloudFunctionResult;
import com.jokls.jok.rpc.t2.base.ServiceDefinition;
import com.jokls.jok.rpc.t2.base.ServiceDefinitionContainer;
import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.rpc.t2.definition.type.complex.IDatasetJokType;
import com.jokls.jok.rpc.t2.definition.type.complex.IDatasetsJokType;
import com.jokls.jok.rpc.t2.definition.type.complex.ParamJokType;
import com.jokls.jok.rpc.t2.definition.type.complex.SimpleMapJokType;
import com.jokls.jok.rpc.t2.definition.type.primitive.*;
import com.jokls.jok.rpc.t2.impl.DefaultServiceDefinitionContainer;
import com.jokls.jok.rpc.t2.impl.TypeDefinitionContainerImpl;
import com.jokls.jok.rpc.t2.impl.TypeObjectParser;
import com.jokls.jok.rpc.t2.impl.annotation.DefPropertiesParser;
import com.jokls.jok.rpc.t2.impl.annotation.PropertiesParser;
import com.jokls.jok.util.ParamNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:20
 */
public class ServiceDefinitionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDefinitionUtil.class);

    public static final String TAG_VALUE = "value";
    public static final String DEFAULT_RESULT_NAME = "result";
    public static final String DEFAULT_PARAM_NAME = "param";
    private static TypeDefinitionContainer typeDefContainer;
    private static ServiceDefinitionContainer serviceDefContainer;
    private static Boolean t2Compress;
    TypeObjectParser typeObjectParser;
    private static PropertiesParser propertiesDefParser;

    public ServiceDefinitionUtil(boolean t2Compress){
        typeDefContainer = new TypeDefinitionContainerImpl();

        typeDefContainer.addType(new BooleanJokType());
        typeDefContainer.addType(new ByteArrayJokType());
        typeDefContainer.addType(new ByteJokType());
        typeDefContainer.addType(new CharacterJokType());
        typeDefContainer.addType(new DoubleJokType());
        typeDefContainer.addType(new FloatJokType());
        typeDefContainer.addType(new IntJokType());
        typeDefContainer.addType(new LongJokType());
        typeDefContainer.addType(new ShortJokType());
        typeDefContainer.addType(new StringJokType());
        typeDefContainer.addType(new BigDecimalJokType());
        typeDefContainer.addType(new DateJokType());
        typeDefContainer.addType(new TimestampJokType());
        typeDefContainer.addType(new IDatasetJokType());
        typeDefContainer.addType(new IDatasetsJokType());
        typeDefContainer.addType(new SimpleMapJokType());

        propertiesDefParser = new DefPropertiesParser();
        serviceDefContainer = new DefaultServiceDefinitionContainer();
        this.typeObjectParser = new TypeObjectParser();
        this.typeObjectParser.setTypeContainer(typeDefContainer);
        this.typeObjectParser.setPropertiesParser(propertiesDefParser);
        ServiceDefinitionUtil.t2Compress = t2Compress;
    }

    public static ServiceDefinition parse(Method method, String functionId){
        String serviceId = "";
        String desc = "" ;
        String type = "" ;
        boolean async = false;
        CloudFunction annotation = AnnotatedElementUtils.findMergedAnnotation(method, CloudFunction.class);
        if(annotation != null){
            serviceId = annotation.serviceId();
            if(StringUtils.isEmpty(functionId)){
                functionId = annotation.functionId();
                if(StringUtils.isEmpty(functionId)){
                    functionId = annotation.value();
                }
            }
            desc = annotation.dec();
            type = annotation.type();
            async = annotation.async();
        }

        ServiceDefinition def = new ServiceDefinition();
        try{
            if(!StringUtils.isEmpty(serviceId)){
                def.setServiceId(serviceId);
            }else{
                serviceId = method.toString();
                def.setServiceId(serviceId);
            }

            if(!StringUtils.isEmpty(functionId)){
                def.setFunctionId(functionId);
                def.setFunctionIds(CloudConstants.COMMA_SPLIT_PATTERN.split(functionId));
            }

            def.setDesc(desc);
            def.setType(type);
            def.setAsync(async);
            def.setServiceMethod(method);

            Parameter reqParam = new Parameter();
            List<Parameter> request = parseRequest(method);
            ParamJokType reqParamType = new ParamJokType();
            reqParamType.setSubTypes(request);
            reqParam.setType(reqParamType);
            def.setRequest(reqParam);

            Parameter resParam = new Parameter();
            List<Parameter> response = parseResponse(method);
            ParamJokType resParamType = new ParamJokType();
            resParamType.setSubTypes(response);
            resParam.setType(resParamType);
            def.setResponse(resParam);

        } catch (Exception e) {
            logger.error("解析接口方法异常: method:" + method.getName(), e);
        }

        return def;
    }

    private static List<Parameter> parseRequest(Method method) throws Exception {
        List<Parameter> parameters = new ArrayList();
        Annotation[][] annotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        JokType jresType = null;
        Class<?> clazz = null;
        Parameter parameter = null;

        for(int i = 0; i < parameterTypes.length; ++i) {
            String temp = null;
            parameter = new Parameter();
            parameter.setParameterType(parameterTypes[i]);
            Annotation annotation = getParameterAnnotation(annotations, i);
            if (annotation != null) {
                temp = AnnotationUtils.getValue(annotation, TAG_VALUE).toString();
            }

            if (!org.springframework.util.StringUtils.isEmpty(temp)) {
                parameter.setTransportName(temp);
                parameter.setJavaName(ParamNameUtils.underScore2CamelCase(temp));
            } else {
                String paramName = DEFAULT_PARAM_NAME + i;
                parameter.setTransportName(paramName);
                parameter.setJavaName(paramName);
            }

            if (isArrayType(parameterTypes[i])) {
                parameter.setArray(true);
                Type javaType = ((ParameterizedType)genericParameterTypes[i]).getActualTypeArguments()[0];
                if (javaType instanceof ParameterizedType) {
                    javaType = ((ParameterizedType)javaType).getRawType();
                    clazz = (Class)javaType;
                } else {
                    clazz = (Class)javaType;
                }

                parameter.setJavaType(JsonUtils.constructParametricType(parameterTypes[i], clazz));
                jresType = getParamType(clazz.getName(), null);
            } else {
                try {
                    if (genericParameterTypes[i] instanceof ParameterizedType) {
                        Type[] javaTypes = ((ParameterizedType)genericParameterTypes[i]).getActualTypeArguments();
                        if (javaTypes != null && javaTypes.length > 0) {
                            Class<?>[] clazzes = new Class[javaTypes.length];
//                            int j = 0;

                            for(int k = 0; k < javaTypes.length; ++k) {
                                Type javaType = javaTypes[k];
                                if (javaType instanceof ParameterizedType) {
                                    clazzes[k] = (Class)((ParameterizedType)javaType).getRawType();
                                } else {
                                    clazzes[k] = (Class)javaType;
                                }

//                                ++j;
                            }

                            parameter.setJavaType(JsonUtils.constructParametricType(parameterTypes[i], clazzes));
                        }
                    }
                } catch (Exception e) {
                }

                parameter.setArray(false);
                jresType = getParamType(parameterTypes[i].getName(), null);
            }

            parameter.setType(jresType);
            parameters.add(parameter);
        }

        return parameters;
    }

    private static List<Parameter> parseResponse(Method method) throws Exception {
        List<Parameter> parameterList = new ArrayList();
        Parameter parameter = new Parameter();
        String resultName = DEFAULT_RESULT_NAME;
        CloudFunctionResult annoServiceResult = AnnotatedElementUtils.findMergedAnnotation(method, CloudFunctionResult.class);

        String resultValue = annoServiceResult.value();
        if (!org.springframework.util.StringUtils.isEmpty(resultValue)) {
            resultName = resultValue;
        }

        parameter.setTransportName(resultName);
        parameter.setJavaName(resultName);
        Type genericReturnType = method.getGenericReturnType();
        Class<?> returnType = method.getReturnType();
        parameter.setParameterType(returnType);
        JokType jresType = null;
        Class<?> clazz = null;
        if (isArrayType(returnType)) {
            parameter.setArray(true);
            Type javaType = ((ParameterizedType)genericReturnType).getActualTypeArguments()[0];
            if (javaType instanceof ParameterizedType) {
                javaType = ((ParameterizedType)javaType).getRawType();
                clazz = (Class)javaType;
            } else {
                clazz = (Class)javaType;
            }

            parameter.setJavaType(JsonUtils.constructParametricType(returnType, clazz));
            jresType = getParamType(clazz.getName(), null);
        } else if (Void.TYPE.isAssignableFrom(returnType)) {
            parameter.setArray(false);
        } else {
            try {
                if (genericReturnType instanceof ParameterizedType) {
                    Type[] javaTypes = ((ParameterizedType)genericReturnType).getActualTypeArguments();
                    if (javaTypes != null && javaTypes.length > 0) {
                        Class<?>[] clazzes = new Class[javaTypes.length];

                        for(int i = 0; i < javaTypes.length; ++i) {
                            Type javaType = javaTypes[i];
                            if (javaType instanceof ParameterizedType) {
                                clazzes[i] = (Class)((ParameterizedType)javaType).getRawType();
                            } else {
                                clazzes[i] = (Class)javaType;
                            }

                        }

                        parameter.setJavaType(JsonUtils.constructParametricType(returnType, clazzes));
                    }
                }
            } catch (Exception e) {
            }

            parameter.setArray(false);
            jresType = getParamType(returnType.getName(), null);
        }

        parameter.setType(jresType);
        parameterList.add(parameter);
        return parameterList;
    }

    private static JokType getParamType(String typeName, String objectType) {
        JokType type = typeDefContainer.getType(typeName);
        if (type == null) {
            type = propertiesDefParser.getParamType(typeName, typeDefContainer);
        }

        return type;
    }

    private static boolean isArrayType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    private static Annotation getParameterAnnotation(Annotation[][] annotations, int index) {
        for(int i = 0; i < annotations[index].length; ++i) {
            if (annotations[index][i].annotationType().equals(CloudFunctionParam.class)) {
                return annotations[index][i];
            }
        }

        return null;
    }

    public static TypeDefinitionContainer getTypeDefContainer() {
        return typeDefContainer;
    }

    public static ServiceDefinitionContainer getServiceDefContainer() {
        return serviceDefContainer;
    }

    public static PropertiesParser getPropertiesDefParser() {
        return propertiesDefParser;
    }

    public static void addDefinition(ServiceDefinition definition) {
        getServiceDefContainer().addDefinition(definition);
    }

    public static void addDefinition(List<ServiceDefinition> definitions) {
        getServiceDefContainer().addDefinitions(definitions);
    }

    public static ServiceDefinition getServiceDefinition(String serviceId, String functionId) {
        ServiceDefinition definition = getServiceDefContainer().getServiceDefinition(serviceId, functionId);
        return definition;
    }

    public static ServiceDefinition getServiceDefinition(Method method) {
        ServiceDefinition definition = getServiceDefContainer().getServiceDefinition(method);
        if (definition == null) {
            synchronized(serviceDefContainer) {
                definition = getServiceDefContainer().getServiceDefinition(method);
                if (definition == null) {
                    definition = parse(method, null);
                    if (definition != null) {
                        getServiceDefContainer().addDefinition(definition);
                    }
                }
            }
        }

        return definition;
    }

    public static Boolean getT2Compress() {
        return t2Compress;
    }

}
