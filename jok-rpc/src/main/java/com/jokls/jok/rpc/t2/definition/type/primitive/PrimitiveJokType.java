package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.PrimitiveTypeConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:50
 */
public class PrimitiveJokType extends SimpleJokType {
    private PrimitiveTypeConvertor convertor;

    PrimitiveJokType(Class clazz, Object value) {
        super(clazz.getSimpleName());
        this.convertor = new PrimitiveTypeConvertor(clazz, value);
    }

    public TypeConvertor getTypeConvertor() {
        return this.convertor;
    }
}
