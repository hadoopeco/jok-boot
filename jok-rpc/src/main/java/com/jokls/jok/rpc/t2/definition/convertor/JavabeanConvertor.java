package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.parameter.Javabean2DatasetUtil;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.util.DatasetUtil;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:35
 */
public class JavabeanConvertor implements TypeConvertor {
    @Override
    public Object encode(Object obj, Parameter parameter, String charset) {
        if (obj == null) {
            return null;
        } else {
            IDataset ds = Javabean2DatasetUtil.encode(obj, parameter, charset);
            return DatasetUtil.pack(ds, charset);
        }
    }

    public Object decode(Object obj, Parameter parameter, String charset) {
        if (obj == null) {
            return null;
        } else {
            byte[] bobj = (byte[])((byte[])obj);
            if (bobj.length == 0) {
                return null;
            } else {
                IDataset ds = DatasetUtil.unpack(bobj, charset);
                if (ds == null) {
                    return null;
                } else {
                    Object result = Javabean2DatasetUtil.decode(ds, parameter, charset);
                    return result;
                }
            }
        }
    }
}
