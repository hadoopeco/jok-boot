package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:47
 */
public class ByteArrayConvertor implements Convertor<byte[]> {
    @Override
    public byte[] convert(IDataset dataset, String name) {
        return dataset.getByteArray(name);
    }
}
