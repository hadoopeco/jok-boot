package com.jokls.jok.dataset.reader;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasetBase;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/17 13:34
 */
public interface IResultSetReader extends IDatasetBase {
    int getInt(String columnName);

    int getInt(int columnIndex);

    int getInt(String columnName, int def);

    int getInt(int columnIndex, int def);

    long getLong(String columnName);

    long getLong(int columnIndex);

    long getLong(String columnName, long def);

    long getLong(int columnIndex, long def);

    double getDouble(String columnName);

    double getDouble(int columnIndex);

    double getDouble(String columnName, double def);

    double getDouble(int columnIndex, double def);

    byte[] getByteArray(String columnName);

    byte[] getByteArray(int columnIndex);

    byte[] getByteArray(String columnName, byte[] def);

    byte[] getByteArray(int columnIndex, byte[] def);

    String getString(String columnName);

    String getString(int columnIndex);

    String getString(String columnName, String def);

    String getString(int columnIndex, String def);

    String[] getStringArray(String columnName);

    String[] getStringArray(int columnIndex);

    String[] getStringArray(String columnName, String[] def);

    IDataset getSubDataset(int columnIndex);

    IDataset getSubDataset(String columnName);

    String[] getStringArray(int columnIndex, String[] def);

    Object getValue(String columnName);

    Object getValue(int columnIndex);

    Object getValue(String columnName, Object def);

    Object getValue(int columnIndex, Object def);

    void locateLine(int columnIndex);

    void beforeFirst();

    boolean hasNext();

    void next();
}
