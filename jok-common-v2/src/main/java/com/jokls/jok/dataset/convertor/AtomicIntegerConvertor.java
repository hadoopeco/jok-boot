package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:02
 */
public class AtomicIntegerConvertor implements Convertor<AtomicInteger> {

    @Override
    public AtomicInteger convert(IDataset dataset, String name) {
        int i = dataset.getInt(name);
        return new AtomicInteger(i);
    }
}
