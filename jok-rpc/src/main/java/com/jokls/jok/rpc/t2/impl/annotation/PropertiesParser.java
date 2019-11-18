package com.jokls.jok.rpc.t2.impl.annotation;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import com.jokls.jok.rpc.t2.definition.type.JokType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:29
 */
public abstract class PropertiesParser {

    public abstract JokType getParamType(String typeName, TypeDefinitionContainer typeContainer);

    public static boolean isValidProperty(Class<?> objectClass, String fieldName) {
        return hasGetSetMethod(objectClass, fieldName) || hasIsSetMethod(objectClass, fieldName);
    }

    public abstract IDataset getObjectType(Object target);

    private static boolean hasGetSetMethod(Class objectClass, String fieldName) {
        return hasGetMethod(objectClass, fieldName) && hasSetMethod(objectClass, fieldName);
    }

    private static boolean hasIsSetMethod(Class objectClass, String fieldName) {
        return hasIsMethod(objectClass, fieldName) && hasSetMethod(objectClass, fieldName);
    }

    private static boolean hasGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        String firstChar = fieldName.substring(0, 1);
        if (fieldName.length() == 1) {
            firstChar = firstChar.toUpperCase();
        } else if (fieldName.length() > 1) {
            String secondChar = fieldName.substring(1, 2);
            boolean isUpperCase = isUpperCase(secondChar);
            if (!isUpperCase) {
                firstChar = firstChar.toUpperCase();
            }
        }

        sb.append("get");
        sb.append(firstChar);
        sb.append(fieldName.substring(1));

        try {
            boolean hasGet = objectClass.getMethod(sb.toString()) != null;
            return hasGet;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isUpperCase(String secondChar) {
        boolean isInUpperCase = false;

        for(int i = 0; i < secondChar.length(); ++i) {
            char c = secondChar.charAt(i);
            if (Character.isUpperCase(c)) {
                isInUpperCase = true;
                break;
            }
        }

        return isInUpperCase;
    }

    private static boolean hasSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            String firstChar = fieldName.substring(0, 1);
            if (fieldName.length() == 1) {
                firstChar = firstChar.toUpperCase();
            } else if (fieldName.length() > 1) {
                String secondChar = fieldName.substring(1, 2);
                boolean isUpperCase = isUpperCase(secondChar);
                if (!isUpperCase) {
                    firstChar = firstChar.toUpperCase();
                }
            }

            sb.append("set");
            sb.append(firstChar);
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean hasIsMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("is");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));

        try {
            return objectClass.getMethod(sb.toString()) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
