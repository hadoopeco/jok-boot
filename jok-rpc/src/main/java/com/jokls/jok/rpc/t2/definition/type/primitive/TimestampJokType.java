package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.TimestampConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:11
 */
public class TimestampJokType extends SimpleJokType {
    private TypeConvertor convertor = new TimestampConvertor();

    public TimestampJokType() {
        super("Timestamp");
    }

    public TypeConvertor getTypeConvertor() {
        return this.convertor;
    }

    public char getTransType() {
        return 'L';
    }
}
