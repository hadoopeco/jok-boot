package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.util.DatasetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:15
 */
public class IDatasetConvertor implements TypeConvertor{
    private static final Logger logger = LoggerFactory.getLogger(IDatasetConvertor.class);
    @Override
    public Object encode(Object obj, Parameter parameter, String charset) {
        return obj != null && obj.toString().length() != 0 ? DatasetUtil.pack((IDataset)obj, charset) : null;
    }

    @Override
    public Object decode(Object obj, Parameter parameter, String charset) {
        if (obj == null) {
            return null;
        } else {
            byte[] bobj = (byte[])obj;
            IDataset ds = DatasetUtil.unpack(bobj, charset);
            return ds;
        }
    }
}
