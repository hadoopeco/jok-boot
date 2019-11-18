package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:54
 */
public class ByteConvertor implements Convertor<Byte> {


    @Override
    public Byte convert(IDataset dataset, String name) {
        return Integer.valueOf(dataset.getInt(name)).byteValue();
    }
}
