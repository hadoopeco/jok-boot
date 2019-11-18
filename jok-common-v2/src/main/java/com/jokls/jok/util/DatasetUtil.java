package com.jokls.jok.util;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.IPack;
import com.jokls.jok.event.PackService;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:25
 */
public class DatasetUtil {
    public static final String PARAM_PREFIX = "j_arg";
    public static final String EXCEPTION_NAME = "j_exception";

    public static byte[] pack(IDataset ds, String charset){
        IPack pack = PackService.getPacker(2, charset);
        pack.addDataset(ds);
        return pack.Pack();
    }

    public static IDataset unpack(byte[] data, String charset) {
        if (data != null && data.length != 0) {
            IPack pack = PackService.getPacker(data, charset);
            return pack.getDatasetCount() > 0 ? pack.getDataset(0) : null;
        } else {
            return null;
        }
    }

}
