package com.jokls.jok.rpc.t2.definition.type.complex;

import com.jokls.jok.rpc.t2.definition.convertor.SimpleMapConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 18:16
 */
public class SimpleMapJokType extends PropertiesJokType {
    private TypeConvertor typeConvertor = new SimpleMapConvertor();

    public SimpleMapJokType() {
        super("Map");
    }

    public TypeConvertor getTypeConvertor() {
        return this.typeConvertor;
    }
}
