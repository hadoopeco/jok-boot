package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.DateConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:12
 */
public class DateJokType extends SimpleJokType {
    private TypeConvertor convertor = new DateConvertor();

    public DateJokType() {
        super("Date");
    }

    public TypeConvertor getTypeConvertor() {
        return this.convertor;
    }
}
