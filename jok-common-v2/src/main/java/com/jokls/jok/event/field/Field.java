package com.jokls.jok.event.field;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.exception.DatasetRuntimeException;
import com.jokls.jok.util.StringUtil;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:03
 */
public class Field implements Cloneable {
    private String name;
    private char type;

    protected Field(String name, char type){
        if(!StringUtils.isEmpty(name)){
            this.name = name ;
            this.type = type;
        }else{
            throw new DatasetRuntimeException("invalid column name[" + name + "]");
        }
    }

    public Field clone() {
        try {
            return (Field)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public boolean equals(Field obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else {
            return StringUtil.equals(this.name, obj.name) && this.type == obj.type;
        }
    }

    public boolean weakEquals(Field obj) {
        if (obj == null) {
            return false;
        } else {
            return this == obj ? true : StringUtil.equals(this.name, obj.name);
        }
    }

    public String toString() {
        return "[" + this.name + "," + this.type + "]";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getType() {
        return this.type;
    }

    public void setType(char type) {
        this.type = type;
    }
}
