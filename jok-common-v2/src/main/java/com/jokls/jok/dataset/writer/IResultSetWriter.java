package com.jokls.jok.dataset.writer;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.exception.DatasetRuntimeException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/17 18:22
 */
public interface IResultSetWriter {

    void addColumn(String columnName);

    void addColumn(String columnName, int type);

    void modifyColumnType(String columnName, int type) throws DatasetRuntimeException;

    void modifyColumnType(int columnIndex, int value) throws DatasetRuntimeException;

    void updateByteArray(int columnIndex, byte[] value) throws DatasetRuntimeException;

    void updateByteArray(String columnName, byte[] value) throws DatasetRuntimeException;

    void updateSubDataset(int columnIndex, IDataset value) throws DatasetRuntimeException;

    void updateSubDataset(String columnName, IDataset value) throws DatasetRuntimeException;

    void updateDouble(int columnIndex, double value) throws DatasetRuntimeException;

    void updateDouble(String columnName, double value) throws DatasetRuntimeException;

    void updateInt(int columnIndex, int value) throws DatasetRuntimeException;

    void updateInt(String columnName, int value) throws DatasetRuntimeException;

    void updateLong(int columnIndex, long value) throws DatasetRuntimeException;

    void updateLong(String columnName, long value) throws DatasetRuntimeException;

    void updateString(int columnIndex, String value) throws DatasetRuntimeException;

    void updateString(String columnName, String value) throws DatasetRuntimeException;

    void updateStringArray(int columnIndex, String[] value) throws DatasetRuntimeException;

    void updateStringArray(String columnName, String[] value) throws DatasetRuntimeException;

    void updateValue(int columnIndex, Object value);

    void updateValue(String columnName, Object value);

    boolean appendRow();

    void clear();

    void clearAll();
}
