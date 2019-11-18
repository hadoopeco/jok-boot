package com.jokls.jok.dataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 17:13
 */
public interface IDatasetMetaData {
    int getColumnCount();

    char getColumnType(int columnIndex);

    int findColumn(String columnName);

    String getColumnName(int columnIndex);
}
