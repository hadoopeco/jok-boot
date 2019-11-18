package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 16:49
 */
public interface Convertor<T> {

    T convert(IDataset dataset, String name);
}
