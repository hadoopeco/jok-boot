package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.util.DatasetUtil;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 18:17
 */
public class SimpleMapConvertor implements TypeConvertor {
    @Override
    public Object encode(Object obj, Parameter parameter, String charset) {
        if( null != obj){
            IDataset result;
            if(parameter.isArray()){
                result = DatasetService.getDefaultInstance().getDataset( (List)obj);
            }else{
                result = DatasetService.getDefaultInstance().getDataset((Map) obj);

            }
            return DatasetUtil.pack(result, charset);

        }

        return null;

    }

    @Override
    public Object decode(Object obj, Parameter parameter, String charset) {

        Object result = null;
        if(null != obj){
            byte[] bobj = (byte[])obj;
            if(bobj.length > 0){
                IDataset ds = DatasetUtil.unpack(bobj, charset);
                if(ds != null){
                    result =parameter.isArray()? DatasetService.getInstance().transformToListMap(ds) : DatasetService.getDefaultInstance().transformToMap(ds);
                }
            }
        }
        return result;

    }
}
