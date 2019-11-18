package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 10:10
 */
public class ByteArrayConvertor implements TypeConvertor {
    private static final Logger logger = LoggerFactory.getLogger(ByteArrayConvertor.class);
    public ByteArrayConvertor(){

    }

    @Override
    public Object encode(Object obj, Parameter param, String charset) {
        if(obj == null){
            return null;
        }else if(!param.isArray()){
            if(obj instanceof byte[]){
                return obj;
            }else if(obj instanceof String){
                return ((String) obj).getBytes();
            }else{
                Assert.isTrue(false, "invalid type convert");
                return null;
            }
        }else{
            JokType type = param.getType();
            IDataset ds = DatasetService.getDefaultInstance().getDataset();
            ds.addColumn(type.getTypeName(), type.getTransType());
            List array = (List)obj;

            for (Object o : array) {
                ds.appendRow();
                ds.updateByteArray(1, (byte[]) o);
            }
            return DatasetUtil.pack(ds,charset);
        }
    }

    @Override
    public Object decode(Object obj, Parameter parameter, String charset) {
        if(obj == null){
            return null;
        }else if(!parameter.isArray()){
            if(obj instanceof byte[]){
                return obj;
            }else{
                try{
                    return obj.toString().getBytes(charset);
                }catch (UnsupportedEncodingException e){
                    logger.warn("字符串转字节编码不支持！", e);
                    return obj.toString().getBytes();
                }
            }
        } else {
            byte[] bobj = (byte[])obj;
            List<byte[]> listObj = new ArrayList<>();
            IDataset ds = DatasetUtil.unpack(bobj, charset);
            if (ds != null) {
                int rowCount = ds.getRowCount();

                for(int i = 1; i <= rowCount; ++i) {
                    ds.locateLine(i);
                    listObj.add(ds.getByteArray(1));
                }
            }

            return listObj;
        }
    }
}
