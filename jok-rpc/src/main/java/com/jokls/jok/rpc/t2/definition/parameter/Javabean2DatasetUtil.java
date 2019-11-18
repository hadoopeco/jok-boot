package com.jokls.jok.rpc.t2.definition.parameter;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.pack.PackV2;
import com.jokls.jok.exception.ParseParamsException;
import com.jokls.jok.rpc.t2.base.TypeContainer;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.rpc.t2.definition.type.complex.JavabeanJokType;
import com.jokls.jok.rpc.t2.impl.TypeObjectParser;
import com.jokls.jok.util.PropertyUtil;
import com.jokls.jok.util.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:20
 */
public class Javabean2DatasetUtil {


    public  static IDataset encode(Object obj, Parameter parameter, String charset){
        if(obj != null){
            IDataset ds = DatasetService.getDefaultInstance().getDataset();
            JavabeanJokType type = (JavabeanJokType) parameter.getType();
            List<Parameter> properties = type.getSubTypes();

            if(!CollectionUtils.isEmpty(properties)){

                for (Parameter property : properties) {
                    if (!property.isArray() && null != property.getType()) {
                        ds.addColumn(property.getTransportName(), property.getType().getTransType());
                    } else {
                        ds.addColumn(property.getTransportName(), 82);
                    }
                }

                if (!parameter.isArray()) {
                    ds.appendRow();
                    encodePojo(ds, obj, properties, true, charset);
                    return ds;
                }

                List<?> listBean = (List)obj;
                if (!CollectionUtils.isEmpty(listBean)) {
                    for(int i =0 ;  i < listBean.size(); ++i) {
                        ds.appendRow();
                        if (0 == i) {
                            encodePojo(ds, listBean.get(i), properties, true, charset);
                        } else {
                            encodePojo(ds, listBean.get(i), properties, false, charset);
                        }
                    }
                }
                return ds;
            }
        }
        return null;
    }


    private static void encodePojo(IDataset ds, Object bean, List<Parameter> properties, boolean flag, String charset){
        TypeObjectParser parser = new TypeObjectParser();

        if(!CollectionUtils.isEmpty(properties)){
            properties.forEach(p ->{
                JokType propertyType =  p.getType();
                Object value = PropertyUtil.getValue(bean, p.getJavaName());
                if( value != null ){
                    if(null != propertyType){
                        if(p.isArray()){
                            List<?> listValue = (List)value;
                            if (!CollectionUtils.isEmpty(listValue)) {
                                 p = parser.getParameter(listValue.get(0), p.getJavaName(), true);
                            }

                        }else{
                            p = parser.getParameter(value, p.getJavaName(), true);
                        }

                        if (null != p.getType() && flag ) {
                            TypeContainer.add(p.getJavaName(), p.getType().getTypeName());
                        }
                    }

                    propertyType = p.getType();
                    if (null != propertyType) {
                        TypeConvertor convertor = propertyType.getTypeConvertor();
                        ds.updateValue(p.getTransportName(), convertor.encode(value, p, charset));
                    }
                }

            });
        }
    }

    public static Object decode(IDataset ds, Parameter parameter, String charset){
        JavabeanJokType type = (JavabeanJokType) parameter.getType();

        try{
            if(!parameter.isArray()){
                ds.locateLine(1);
                return null;
            } else {
                List<Object> listObj = new ArrayList<>();
                int rowCount  = ds.getRowCount();
                for(int i=1; i <= rowCount; i++){
                    ds.locateLine(i);
                    listObj.add(decodePojo(ds, type, true, charset));
                }

                return listObj;
            }
        }catch (Exception e){
            throw new ParseParamsException("Failed to decode pojo", e);
        }
    }

    private static Object decodePojo(IDataset ds, JavabeanJokType type, boolean isArray, String charset) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Object bean = type.getTypeClass().newInstance();
        List<Parameter> properties = type.getSubTypes();
        TypeObjectParser parser = new TypeObjectParser();
        Map<String, String> types = getTypes();
        if (CollectionUtils.isEmpty(properties)) {
            return bean;
        } else {
            Parameter property = null;
            JokType propertyType = null;
            String typeName = null;
            TypeConvertor convertor = null;
            Object dsValue = null;
            Object value = null;
            int i = 0;

            for(int size = properties.size(); i < size; ++i) {
                property = properties.get(i);
                propertyType = property.getType();
                if (null == propertyType) {
                    if (CollectionUtils.isEmpty(types)) {
                        continue;
                    }

                    typeName = types.get(property.getJavaName());
                    property = parser.getParameter(typeName, property.getJavaName(), property.isArray());
                    if (property != null) {
                        propertyType = property.getType();
                    }
                }

                if (null != propertyType) {
                    convertor = propertyType.getTypeConvertor();
                    dsValue = ds.getValue(property.getTransportName());
                    if (null != dsValue) {
                        value = convertor.decode(dsValue, property, charset);
                    } else {
                        value = null;
                    }

                    if (null == value) {
                        PropertyUtils.setProperty(bean, property.getJavaName(), null);
                    } else if (value instanceof Collection) {
                        if (PropertyUtils.isWriteable(bean, property.getJavaName())) {
                            PropertyUtils.setProperty(bean, property.getJavaName(), value);
                        } else if (PropertyUtils.isReadable(bean, property.getJavaName())) {
                            Object obj = PropertyUtils.getProperty(bean, property.getJavaName());
                            if (obj instanceof Collection) {
                                ((Collection)obj).addAll((Collection)value);
                            }
                        }
                    } else {
                        String isUseNull = PackV2.useNullMap.get(PackV2.NULL_KEY);
                        if (!StringUtils.isEmpty(isUseNull) && "true".equals(isUseNull)) {
                            String nullStr = PackV2.useNullMap.get(PackV2.NULL_STRING_KEY);
                            String tmpStr = StringUtils.isEmpty(nullStr) ? PackV2.NULL_STRING : nullStr;
                            if (StringUtil.equals(tmpStr, value.toString())) {
                                PropertyUtils.setProperty(bean, property.getJavaName(), null);
                            } else {
                                PropertyUtils.setProperty(bean, property.getJavaName(), value);
                            }
                        } else {
                            PropertyUtils.setProperty(bean, property.getJavaName(), value);
                        }
                    }
                }
            }

            return bean;
        }
    }



    private static Map<String, String> getTypes(){
        IDataset ds = TypeContainer.get();
        if(ds != null && ds.getRowCount() != 0){
            Map<String,String> types = new HashMap<>();
            for(int i=1 ; i < ds.getRowCount(); i++){
                ds.locateLine(i);
                types.put(ds.getString("key"), ds.getString("type"));
            }
            return types;
        }
        return null;
    }

}
