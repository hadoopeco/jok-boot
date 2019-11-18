package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.pack.PackV2;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;
import com.jokls.jok.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 9:02
 */
public class StringConvertor implements TypeConvertor {
    private static final Logger logger = LoggerFactory.getLogger(StringConvertor.class);

    @Override
    public Object encode(Object obj, Parameter param, String charset) {
        if(obj == null){
            return null;
        }else if(param.isArray()){
            JokType type = param.getType();
            IDataset ds = DatasetService.getDefaultInstance().getDataset();
            ds.addColumn(type.getTypeName(), type.getTransType());

            for(Object value : (List)obj){
                if(value != null){
                    ds.updateValue(1, value.toString());
                }
            }

            return DatasetUtil.pack(ds, charset);
        }else {
            return obj.toString();
        }
    }

    @Override
    public Object decode(Object obj, Parameter parameter, String charset) {
        if( obj == null){
            return null;
        }else if(!parameter.isArray()){
            if(obj instanceof byte[]){
                try{
                    return new String((byte[])obj, charset);
                }catch (UnsupportedEncodingException e){
                    logger.warn("字节转字符串编码不支持！", e);
                    return new String((byte[])obj);
                }
            }else {
                String val = obj.toString();
                String isUserNull = PackV2.useNullMap.get(PackV2.NULL_KEY);
                if(!StringUtils.isEmpty(isUserNull)){
                    String nullStr = PackV2.useNullMap.get(PackV2.NULL_STRING_KEY);
                    String tmpStr = StringUtils.isEmpty(nullStr) ? PackV2.NULL_STRING : nullStr;
                    if(StringUtil.equals(tmpStr,val)) {
                        if ("true".equals(isUserNull)) {
                            val = null;
                        }else{
                            val = "";
                        }
                    }
                }
                return val;

            }
        }else{
            byte[] bobj = (byte[]) obj;
            List results = new ArrayList();
            IDataset ds = DatasetUtil.unpack(bobj, charset);

            if ( ds != null){
                for(int i = 1; i <= ds.getRowCount() ; ++i){
                    ds.locateLine(i);
                    results.add(ds.getString(1));
                }
            }
            return results;
         }
    }
}
