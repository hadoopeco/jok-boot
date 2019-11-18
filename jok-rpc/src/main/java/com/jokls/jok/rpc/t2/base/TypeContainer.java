package com.jokls.jok.rpc.t2.base;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 10:56
 */
public class TypeContainer {
    private static ThreadLocal<IDataset> local = new ThreadLocal();

    public TypeContainer() {
    }

    public static void add(String key, String value) {
        IDataset ds = local.get();
        if (null == ds) {
            IDataset temp = DatasetService.getDefaultInstance().getDataset();
            temp.setDatasetName("typeDs");
            ds = temp;
            temp.addColumn("key", 83);
            temp.addColumn("type", 83);
            local.set(temp);
        }

        ds.appendRow();
        ds.updateString("key", key);
        ds.updateString("type", value);
    }

    public static void put(IDataset ds) {
        local.set(ds);
    }

    public static IDataset get() {
        if (null == local.get()) {
            IDataset temp = DatasetService.getDefaultInstance().getDataset();
            temp.setDatasetName("typeDs");
            return temp;
        } else {
            return local.get();
        }
    }

    public static void clear() {
        local.remove();
    }
}
