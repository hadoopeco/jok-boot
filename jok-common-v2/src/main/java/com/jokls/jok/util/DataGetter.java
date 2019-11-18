package com.jokls.jok.util;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 10:32
 */
public class DataGetter {

    public static int getInt(Object value, int defaultValue){
        int result = defaultValue;
        if (value != null){
            try{
                result = Integer.valueOf(value.toString());
            }catch (NumberFormatException e){

            }
        }

        return result;
    }


    public static boolean getBoolean(Object value, boolean defaultValue) {
        boolean resultValue = defaultValue;
        if(value != null){
            resultValue = Boolean.valueOf(value.toString());
        }

        return resultValue;
    }
}
