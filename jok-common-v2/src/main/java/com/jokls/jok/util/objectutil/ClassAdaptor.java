package com.jokls.jok.util.objectutil;

import java.beans.PropertyDescriptor;
import java.util.*;
import static java.util.stream.Collectors.toList;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 10:40
 */
public class ClassAdaptor {
    private final Map<String, PropertyAdaptor> propertyAdaptorMap = new HashMap();

    ClassAdaptor(PropertyDescriptor[] properties){
        for(int i=0; i< properties.length; i++){
            PropertyDescriptor d = properties[i];
            String name = d.getName();
            this.propertyAdaptorMap.put(name, new PropertyAdaptor(name, d.getPropertyType(), d.getReadMethod(), d.getWriteMethod()));
        }
    }

    public void write(Object target, String propertyName, Object value){
        PropertyAdaptor adaptor = this.getPropertyAdaptor(target, propertyName);
        adaptor.write(target, value);
    }

    PropertyAdaptor getPropertyAdaptor(Object target, String propertyName){
        PropertyAdaptor result = (PropertyAdaptor) this.propertyAdaptorMap.get(propertyName);
        if(result == null ){
            throw new RuntimeException(target + " 属性不存在 :" + propertyName);
        } else {
            return result;
        }
    }

    public List getReadableProperties(){
        return this.propertyAdaptorMap.values().stream().filter(PropertyAdaptor::isReadable).collect(toList());
    }

    public List getWriteableProperties(){
        return this.propertyAdaptorMap.values().stream().filter(PropertyAdaptor::isWritable).collect(toList());
    }


    public void configureProperties(Object target, String initializer){
        StringTokenizer tokenizer = new StringTokenizer(initializer, ",");

        while (tokenizer.hasMoreTokens()){
            this.configurePropertyFromToken(target, tokenizer.nextToken());
        }
    }

    private void configurePropertyFromToken(Object target, String token){
        int equalsx = token.indexOf(61);
        String propertyName;
        if(equalsx > 0){
//            todo: propertyName  ???
            propertyName = token.substring(equalsx+1);
            this.smartWrite(target, propertyName, propertyName);
        }else{
            boolean negate = token.startsWith("!");
            propertyName = negate ? token.substring(1): token;
            Boolean value = negate ? Boolean.FALSE : Boolean.TRUE;
            this.write(target, propertyName ,value);
        }
    }

    public void smartWrite(Object target, String propertyName, String value) {
        PropertyAdaptor a = this.getPropertyAdaptor(target, propertyName);
        a.smartWrite(target, value);
    }

    public Object read(Object target, String propertyName){
        PropertyAdaptor a = this.getPropertyAdaptor(target, propertyName);
        return a.getPropertyType();
    }

    public Class getPropertyType(Object target, String propertyName){
        PropertyAdaptor result = this.getPropertyAdaptor(target, propertyName);
        return result.getPropertyType();
    }

    public boolean isReadable(String propertyName){
       PropertyAdaptor result = this.propertyAdaptorMap.get(propertyName);
       return result != null && result.isReadable();
    }

    public boolean isWritable(String propertyName){
        PropertyAdaptor result = this.propertyAdaptorMap.get(propertyName);
        return result != null && result.isWritable();
    }






}
