package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.writer.IConvertor;

import java.util.Date;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:05
 */
public class LongToDateConvertor implements IConvertor {
    @Override
    public Object convert(Object value) {
        long val = Long.valueOf(value.toString());
        return (new Date(val)).toString();
    }


}
