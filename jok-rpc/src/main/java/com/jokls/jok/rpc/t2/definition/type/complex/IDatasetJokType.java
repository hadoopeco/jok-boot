package com.jokls.jok.rpc.t2.definition.type.complex;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.convertor.IDatasetConvertor;
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
public class IDatasetJokType extends SimpleJokType {
    private TypeConvertor typeConvertor = new IDatasetConvertor();

    public IDatasetJokType() {
        super(IDataset.class.getName());
    }

    public TypeConvertor getTypeConvertor() {
        return this.typeConvertor;
    }
}
