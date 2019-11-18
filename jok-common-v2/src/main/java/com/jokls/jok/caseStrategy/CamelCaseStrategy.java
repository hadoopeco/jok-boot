package com.jokls.jok.caseStrategy;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:06
 */
public class CamelCaseStrategy extends BaseCaseStrategy {
    @Override
    public String getPropertyName(String fieldName) {
        fieldName = fieldName.toLowerCase();
        String[] sa = fieldName.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append(sa[0]);

        for(String s : sa){
            sb.append(this.uppercaseFirstChar(s));
        }
        return sb.toString();
    }

    @Override
    public String getFieldName(String propertyName) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < propertyName.length(); ++i) {
            if (propertyName.charAt(i) >= 'A' && propertyName.charAt(i) <= 'Z') {
                sb.append("_").append((char)(propertyName.charAt(i) + 32));
            } else {
                sb.append(propertyName.charAt(i));
            }
        }

        return sb.toString();
    }

    private String uppercaseFirstChar(String str) {
        String ret = str;
        int c = str.charAt(0);
        if ( c <= 'z' && c >= 'a') {
            c = c - 32;
            ret = (char)c + str.substring(1);
        }

        return ret;
    }
}
