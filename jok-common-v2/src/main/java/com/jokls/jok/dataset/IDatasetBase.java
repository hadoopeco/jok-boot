package com.jokls.jok.dataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/17 13:33
 */
public interface IDatasetBase {
    int MODE_EXCEPTION = 0;
    int MODE_DEFAULT = 1;

    int getMode();

    void setMode(int mode);

    int getRowCount();
}
