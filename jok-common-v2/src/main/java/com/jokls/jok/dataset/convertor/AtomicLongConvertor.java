package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:06
 */
public class AtomicLongConvertor implements Convertor<AtomicLong> {

    @Override
    public AtomicLong convert(IDataset dataset, String name) {
        long l = dataset.getLong(name);
        return new AtomicLong(l);
    }
}
