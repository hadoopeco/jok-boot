package com.jokls.jok.common.util;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:45
 */
public class StringUtils {
    public StringUtils() {
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return null != str && !"".equals(str);
    }

    public static boolean equals(String str1, String str2) {
        try {
            return str1.equals(str2);
        } catch (Exception e) {
            return false;
        }
    }
}
