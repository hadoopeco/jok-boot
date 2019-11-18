package com.jokls.jok.dataset;

import com.jokls.jok.exception.DatasetRuntimeException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:15
 */
public interface IDatasets {
    void putDataset(IDataset dataset) throws DatasetRuntimeException;

    IDataset getDataset(String name);

    String getDatasetName(int index);

    IDataset getDataset(int index);

    int getDatasetCount();

    void clear();
}
