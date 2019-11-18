package com.jokls.jok.event;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:29
 */
public interface IPack {
    char SPLIT_V1 = '\u0001';
    char SPLIT_V2 = '\u0000';
    int VERSION_1 = 1;
    int VERSION_2 = 2;
    int VERSION_3 = 3;

    void addDataset(IDataset ds);

    boolean unPack(byte[] bytes);

    byte[] Pack();

    IDataset getDataset(int index);

    int getVersion();

    int getDatasetCount();

    void clear();

    void setCharset(String charset);
}
