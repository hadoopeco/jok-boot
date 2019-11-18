package com.jokls.jok.dataset;

import com.jokls.jok.dataset.reader.IResultSetReader;
import com.jokls.jok.dataset.writer.IResultSetWriter;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:15
 */
public interface IDataset extends IResultSetReader,IDatasetMetaData, IResultSetWriter {
    String DS_DEFAULT_NAME = "PLATFORM_DEFAULT_DATASET_NAME";
    String DS_PARAMETERS = "PLATFORM_PARAMETER_DATASET_NAME";

    String getDatasetName();

    void setDatasetName(String datasetName);

    int getTotalCount();

    void setTotalCount(int totalCount);

    IDatasetMetaData getMetaData();

    void deleteRow(int rowIndex);
}
