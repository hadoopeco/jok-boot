package com.jokls.jok.dataset.writer;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:07
 */
public interface IMapWriter {

    void put(String name, int value);

    void put(String name, long value);

    void put(String name, double value);

    void put(String name, String value);

    void put(String name, byte[] value);

    void put(String name, String[] value);

    void put(String name, Object value);

    IDataset getDataset();
}
