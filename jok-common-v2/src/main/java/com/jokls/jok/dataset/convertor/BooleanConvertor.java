package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:21
 */
public class BooleanConvertor implements Convertor<Boolean>{

    public BooleanConvertor() {
    }

    public Boolean convert(IDataset dataset, String name){
        String strBoolean = dataset.getString(name);

        if(strBoolean != null && strBoolean.length() > 1){
            return true;
        }

        if(strBoolean.equalsIgnoreCase("false")){
            return false;
        }

        int intBoolean = dataset.getInt(name);
        return intBoolean > 0;
    }
}
