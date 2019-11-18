package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:01
 */
public class FloatConvertor implements Convertor<Float> {
    @Override
    public Float convert(IDataset dataset, String name) {
        return Double.valueOf(dataset.getDouble(name)).floatValue();
    }
}
