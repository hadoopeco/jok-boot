package com.jokls.jok.rpc.t2.definition.type.primitive;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:08
 */
public class ShortJokType extends PrimitiveJokType {
    public ShortJokType() {
        super(Short.TYPE, 0);
    }

    public char getTransType() {
        return 'I';
    }
}
