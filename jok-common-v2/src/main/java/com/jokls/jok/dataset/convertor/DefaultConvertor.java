package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 16:51
 */
public class DefaultConvertor<T> implements Convertor<T> {
    private Class<T> clz;

    public DefaultConvertor(Class<T> clz) {
        this.clz = clz;
    }

    public T convert(IDataset dataset, String name) {
        try {
            return this.clz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
