package com.jokls.jok.rpc.t2.definition.type.complex;

import com.jokls.jok.rpc.t2.definition.convertor.JavabeanConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:23
 */
public class JavabeanJokType extends PropertiesJokType {

    private JavabeanConvertor convertor = new JavabeanConvertor();

    public JavabeanJokType(String typeName) {
        super(typeName);
    }

    public TypeConvertor getTypeConvertor() {
        return this.convertor;
    }
}
