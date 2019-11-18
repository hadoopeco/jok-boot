package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.ByteArrayConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 10:43
 */
public class ByteArrayJokType extends SimpleJokType {
    private TypeConvertor typeConvertor = new ByteArrayConvertor();

    public ByteArrayJokType() {
        super("byte[]");
    }

    public TypeConvertor getTypeConvertor() {
        return this.typeConvertor;
    }

    public char getTransType() {
        return 'R';
    }

}
