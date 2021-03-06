package com.jokls.jok.rpc.t2.definition.type.primitive;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:06
 */
public class CharacterJokType extends PrimitiveJokType {
    public CharacterJokType() {
        super(Character.TYPE, ' ');
    }

    public char getTransType() {
        return 'S';
    }
}
