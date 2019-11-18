package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:10
 */
public class StringArrayConvertor implements Convertor<String[]> {
    @Override
    public String[] convert(IDataset dataset, String name) {
        return dataset.getStringArray(name);
    }
}
