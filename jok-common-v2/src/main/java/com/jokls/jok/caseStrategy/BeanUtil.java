package com.jokls.jok.caseStrategy;

import java.lang.reflect.Method;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 16:52
 */
public class BeanUtil {

    public static void setProperty(Object object, String fieldName, Object value) throws Exception {
        Method[] methods = object.getClass().getDeclaredMethods();
        final String methodName = findSetMethod(fieldName);

        boolean flag = true;

        for(Method method : methods){
            if(methodName.equals(method.getName())){
                method.invoke(object, value);
                flag = false;
                break;
            }
        }

        if(flag){
            throw new Exception("未找到相关方法");
        }
    }

    /**
     * find the setter method
     * @param fieldName
     * @return
     */
    private static String findSetMethod(String fieldName) {
        //首字母大写
        char up  = (char)(fieldName.charAt(0) - 32);
        return "set"+up+ fieldName.substring(1);
    }
}
