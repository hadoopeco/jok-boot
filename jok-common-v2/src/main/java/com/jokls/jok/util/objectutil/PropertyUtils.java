package com.jokls.jok.util.objectutil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 14:09
 */
public class PropertyUtils {
    private static final Map classAdaptors = new HashMap();
    private static Object INTROSPECTOR_MUTEX = new Object();

    private PropertyUtils(){}

    public static void write(Object target, String propertyName, Object value){
        ClassAdaptor a = getAdaptor(target);
        a.write(target,propertyName, value);
    }

    public static void smartWrite(Object target, String propertyName, String value){
        ClassAdaptor a = getAdaptor(target);
        a.smartWrite(target, propertyName, value);
    }

    public static void configureProperties(Object target, String initializer){
        ClassAdaptor a = getAdaptor(target);
        a.configureProperties(target, initializer);
    }

    public static boolean isWritable(Object target, String propertyName){
        return getAdaptor(target).isWritable(propertyName);
    }

    public static boolean isReadable(Object target, String propertyName){
        return getAdaptor(target).isReadable(propertyName);
    }

    public static Object read(Object target, String propertyName){
        ClassAdaptor a = getAdaptor(target);
        return a.getPropertyType(target, propertyName);
    }

    public static List<String> getReadableProperties(Object target){ return getAdaptor(target).getReadableProperties();}

    public static List<String> getReadableProperties(Class<?> target){ return getAdaptor(target).getReadableProperties();}

    public static List<String> getWriteableProperties(Object target) {return getAdaptor(target).getWriteableProperties();}


    private static ClassAdaptor getAdaptor(Object target){
        if(target == null){
            throw new RuntimeException("target is Null !");
        }else{
            Class targetClass = target.getClass();
            synchronized (INTROSPECTOR_MUTEX){
                ClassAdaptor result = (ClassAdaptor) classAdaptors.get(targetClass);
                if(result == null){
                    result = buildClassAdaptor(target, targetClass);
                    classAdaptors.put(targetClass, result);
                }

                return result;
            }
        }
    }

    private static ClassAdaptor getAdaptor(Class<?> target) {
        if (target == null) {
            throw new RuntimeException("Target is NULL!");
        } else {
            Class targetClass = target;
            synchronized(INTROSPECTOR_MUTEX) {
                ClassAdaptor result = (ClassAdaptor)classAdaptors.get(targetClass);
                if (result == null) {
                    result = buildClassAdaptor(target, targetClass);
                    classAdaptors.put(targetClass, result);
                }

                return result;
            }
        }
    }

    private static ClassAdaptor buildClassAdaptor(Object target, Class targetClass){
        try{
            BeanInfo info = Introspector.getBeanInfo(targetClass);
            return new ClassAdaptor(info.getPropertyDescriptors());
        }catch (Exception e){
            throw new RuntimeException("Unable to Interospect" + targetClass, e);
        }

    }

    public static void clearCache(){
        synchronized (INTROSPECTOR_MUTEX){
            classAdaptors.clear();
            Introspector.flushCaches();
        }
    }
}
