package com.jokls.jok.rpc.t2.definition.type.primitive;

import com.jokls.jok.rpc.t2.definition.type.JokType;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:38
 */
public abstract class SimpleJokType extends JokType {
    
    public SimpleJokType(String typeName) {
        super(typeName);
    }

    public boolean hasSubTypes() {
        return false;
    }

    public char getTransType() {
        return 'S';
    }
}
