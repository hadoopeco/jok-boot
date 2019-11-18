package com.jokls.jok.dataset.writer;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.exception.DatasetRuntimeException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:13
 */
public interface IColumnModifier {

    void registerConvertor(String var1, int var2, IConvertor var3) throws DatasetRuntimeException;

    void registerConvertor(int var1, int var2, IConvertor var3) throws DatasetRuntimeException;

    IDataset doConvert(IDataset ds);
}
