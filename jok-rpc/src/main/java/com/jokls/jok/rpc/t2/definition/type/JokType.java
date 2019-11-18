package com.jokls.jok.rpc.t2.definition.type;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:28
 */
public abstract class JokType implements Convertable {
    private String typeName;

    public JokType(){

    }

    public JokType(String typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return this.getTypeName();
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public abstract char getTransType();

    public abstract boolean hasSubTypes();
}