package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:02
 */
public class IntConvertor implements Convertor<Integer> {

    @Override
    public Integer convert(IDataset dataset, String name) {
        return dataset.getInt(name);
    }
}
