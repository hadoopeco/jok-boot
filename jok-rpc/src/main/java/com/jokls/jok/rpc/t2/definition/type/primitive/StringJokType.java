package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.StringConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:09
 */
public class StringJokType extends SimpleJokType {
    private TypeConvertor convertor = new StringConvertor();

    public StringJokType() {
        super("String");
    }

    public TypeConvertor getTypeConvertor() {
        return this.convertor;
    }
}
