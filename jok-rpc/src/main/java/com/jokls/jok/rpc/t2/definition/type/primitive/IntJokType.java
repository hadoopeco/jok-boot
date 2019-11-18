package com.jokls.jok.rpc.t2.definition.type.primitive;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:07
 */
public class IntJokType extends PrimitiveJokType {
    public IntJokType() {
        super(Integer.TYPE, 0);
    }

    public char getTransType() {
        return 'I';
    }
}
