package com.jokls.jok.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 10:37
 */
public class ParamNameUtils {

    public static String camelCase2Underscore(String param){
        Pattern p = Pattern.compile("[A-Z]");
        if(param != null && !param.equals("")){
            StringBuilder sb = new StringBuilder(param);
            Matcher matcher = p.matcher(param);

            for(int i =0; matcher.find(); i++){
                sb.replace(matcher.start() +i , matcher.end()+i, "_"+matcher.group().toLowerCase());
            }

            if ('_' == sb.charAt(0)) {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underScore2CamelCase(String name){
        String[] elems = name.split("_");

        for(int i = 0; i < elems.length; ++i) {
            elems[i] = elems[i].toLowerCase();
            if (i != 0) {
                String elem = elems[i];
                char first = elem.toCharArray()[0];
                elems[i] = "" + (char)(first - 32) + elem.substring(1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String el : elems){
            sb.append(el);
        }
        return sb.toString();

    }
}
