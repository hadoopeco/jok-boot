package com.jokls.jok.rpc.t2.definition.type.complex;

import com.jokls.jok.exception.NoSuchTypeException;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.Convertable;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.rpc.t2.definition.type.primitive.SimpleJokType;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:24
 */
public abstract class PropertiesJokType extends JokType implements Convertable {
    protected List<Parameter> subTypes = new ArrayList();
    private Class clz;
    private Boolean isSimplePojo = null;

    public PropertiesJokType() {

    }

    protected PropertiesJokType(String typeName) {
        super(typeName);
    }

    public String toString() {
        if (this.getTypeName() != null) {
            return super.toString();
        } else if (this.clz != null) {
            return this.clz.getName();
        } else {
            return this.hasSubTypes() ? this.subTypes.toString() : super.toString();
        }
    }

    public char getTransType() {
        return 'R';
    }

    public boolean isSimplePojo() {
        if (this.isSimplePojo == null) {
            boolean result = true;
            List<Parameter> params = this.getSubTypes();

            for(int i = 0; i < params.size(); ++i) {
                JokType innerType = ((Parameter)params.get(i)).getType();
                if (!(innerType instanceof SimpleJokType)) {
                    result = false;
                    break;
                }
            }

            this.isSimplePojo = result;
        }

        return this.isSimplePojo;
    }

    public Class getTypeClass() {
        if (this.clz == null) {
            synchronized(this) {
                if (this.clz == null) {
                    try {
                        this.clz = Class.forName(this.getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new NoSuchTypeException("class[" + this.getTypeName() + "]", e);
                    }
                }
            }
        }

        return this.clz;
    }

    public boolean hasSubTypes() {
        return this.subTypes.size() > 0;
    }

    public List<Parameter> getSubTypes() {
        return this.subTypes;
    }

    public void setSubTypes(List<Parameter> subTypes) {
        this.subTypes = subTypes;
    }
}
