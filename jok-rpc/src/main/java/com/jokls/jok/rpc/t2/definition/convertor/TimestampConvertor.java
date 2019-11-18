package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 11:11
 */
public class TimestampConvertor implements TypeConvertor {
    private Pattern validator = Pattern.compile("[\\d]+");

    @Override
    public Object encode(Object obj, Parameter parameter, String charset) {
        if(!StringUtils.isEmpty(obj)){
            if(!parameter.isArray()){
                return this.encodeTimestamp(obj);
            }else{
                JokType type = parameter.getType();
                IDataset ds = DatasetService.getDefaultInstance().getDataset();
                ds.addColumn(type.getTypeName(), type.getTransType());
                List array = (List) obj;

                array.forEach(p -> {
                    ds.appendRow();
                    ds.updateLong(1, this.encodeTimestamp(p));
                });

                return DatasetUtil.pack(ds, charset);
            }
        }else{
            return 0L;
        }
    }

    @Override
    public Object decode(Object obj, Parameter param, String charset) {
        if (StringUtils.isEmpty(obj)){
            if(!param.isArray()){
                if(obj instanceof byte[]){
                    obj = new String((byte[])obj);
                }

                if(this.validator.matcher(obj.toString()).find()){
                    long time = Long.parseLong(obj.toString());
                    return this.decodeTimestamp(time);
                }else{
                    return null;
                }
            }else {
                byte[] bobj = (byte[]) obj;
                List listObj = new ArrayList();
                IDataset ds = DatasetUtil.unpack(bobj, charset);
                if(ds != null){
                    int rowCount = ds.getRowCount();

                    for(int i =1 ; i <= rowCount; i++){
                        ds.locateLine(i);
                        listObj.add(this.decodeTimestamp(ds.getLong(1)));
                    }
                }
                return listObj;
            }
        }else {
            return null;
        }
    }

    private long encodeTimestamp(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp)obj).getTime();
        } else {
            return obj instanceof Number ? ((Number)obj).longValue() : 0L;
        }
    }

    private Timestamp decodeTimestamp(long value) {
        return value == 0L ? null : new Timestamp(value);
    }
}
