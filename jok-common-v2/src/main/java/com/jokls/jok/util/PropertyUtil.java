package com.jokls.jok.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:50
 */
public class PropertyUtil {
    public PropertyUtil() {
    }

    public static Object getValue(Object target, String property) {
        try {
            return PropertyUtils.getProperty(target, property);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
