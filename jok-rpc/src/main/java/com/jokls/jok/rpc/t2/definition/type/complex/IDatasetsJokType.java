package com.jokls.jok.rpc.t2.definition.type.complex;

import com.jokls.jok.dataset.IDatasets;
import com.jokls.jok.rpc.t2.definition.convertor.TypeConvertor;
import com.jokls.jok.rpc.t2.definition.type.primitive.SimpleJokType;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:14
 */
public class IDatasetsJokType extends SimpleJokType {
    public IDatasetsJokType() {
        super(IDatasets.class.getName());
    }

    public TypeConvertor getTypeConvertor() {
        return null;
    }
}
