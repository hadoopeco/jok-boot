package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.pack.PackV2;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;
import com.jokls.jok.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:10
 */
public class BigDecimalConvertor implements TypeConvertor {
    private static final Logger logger = LoggerFactory.getLogger(BigDecimalConvertor.class);

    @Override
    public Object encode(Object obj, Parameter param, String charset) {
        if(obj != null && obj.toString().length() != 0){
            if(param.isArray()){
                JokType type = param.getType();
                IDataset ds = DatasetService.getDefaultInstance().getDataset();
                ds.addColumn(type.getTypeName(), type.getTransType());
                List list = (List)obj;
                list.stream().filter(Objects::nonNull).forEach(o -> ds.updateString(1, o.toString()));

                return DatasetUtil.pack(ds, charset);
            }else {
                return obj.toString();
            }
        } else {
            return null;
        }
    }

    @Override
    public Object decode(Object obj, Parameter param, String charset) {
        if( obj != null && obj.toString().length() != 0){
            if(! param.isArray()){
                if(obj instanceof byte[]){
                    obj = new String((byte[])obj);
                }

                return this.decodeBigDecimal(obj.toString());
            }else {
                byte[] bobj = (byte[])obj;
                IDataset ds = DatasetUtil.unpack(bobj, charset);
                if(ds == null){
                    return null;
                }else {
                    List litObj = new ArrayList();
                    for(int i = 1; i <= ds.getRowCount(); i++ ){
                        ds.locateLine(i+1);
                        litObj.add(this.decodeBigDecimal(ds.getString(1)));
                    }

                    return litObj;
                }
            }
        }else {
            return null;
        }
    }

    private BigDecimal decodeBigDecimal(String value) {
        if (value != null && !StringUtils.isEmpty(value) && !StringUtil.equals(PackV2.NULL_STRING, value)) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                logger.error("failed to decode bigdecimal[" + value + "]", e);
            }
        }

        return null;
    }
}
