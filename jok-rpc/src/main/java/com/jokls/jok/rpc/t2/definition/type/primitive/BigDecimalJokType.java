package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.convertor.BigDecimalConvertor;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:09
 */
public class BigDecimalJokType extends SimpleJokType {
    private TypeConvertor typeConvertor = new BigDecimalConvertor();

    public BigDecimalJokType() {
        super("BigDecimal");
    }

    public TypeConvertor getTypeConvertor() {
        return this.typeConvertor;
    }
}
