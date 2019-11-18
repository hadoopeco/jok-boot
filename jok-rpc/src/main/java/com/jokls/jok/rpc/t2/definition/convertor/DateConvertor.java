package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.pack.PackV2;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;
import com.jokls.jok.util.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 9:34
 */
public class DateConvertor implements TypeConvertor{
    static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public Object encode(Object obj, Parameter param, String charset) {
        if(obj != null && obj.toString().length() != 0){
            DateFormat formater = this.getDateFormat(param);
            if(param.isArray()){
                JokType type = param.getType();
                IDataset ds = DatasetService.getDefaultInstance().getDataset();
                ds.addColumn(type.getTypeName(), type.getTransType());
                List<Date> array = (List<Date>) obj;
                for(Date dt : array){
                    if( dt != null ){
                        ds.updateString(1, formater.format(dt));
                    }
                }
                return DatasetUtil.pack(ds, charset);
            }else {
                return formater.format(obj);
            }
        } else {
            return null;
        }
    }

    @Override
    public Object decode(Object obj, Parameter param, String charset) {
         if(obj != null && obj.toString().length() != 0){
             String isUseNull = PackV2.useNullMap.get(PackV2.NULL_KEY);
             if(!StringUtils.isEmpty(isUseNull)){
                 String nullStr = PackV2.useNullMap.get(PackV2.NULL_STRING_KEY);
                 String tmpStr = StringUtils.isEmpty(nullStr) ? PackV2.NULL_STRING : nullStr;
                 if (StringUtil.equals(tmpStr, obj.toString())) {
                     return null;
                 }
             }

             DateFormat formater = this.getDateFormat(param);
             try{
                 if (!param.isArray()) {
                     if(obj instanceof byte[]){
                         obj = new String((byte[]) obj, charset);
                     }
                     return formater.parse(obj.toString());
                 }else{
                     byte[] bobj = (byte[]) obj;
                     List results = new ArrayList();
                     IDataset ds = DatasetUtil.unpack(bobj, charset);
                     if(ds != null){
                         for (int i= 1; i <= ds.getRowCount(); i++){
                             ds.locateLine(i);
                             String value = ds.getString(1);
                             if(StringUtils.isEmpty(value)){
                                 results.add(formater.parse(value));
                             }else{
                                 results.add(null);
                             }
                         }
                     }
                     return results;
                 }
             }catch (Exception e){
                return null;
             }
         }else {
             return null;
         }
    }

    private DateFormat getDateFormat(Parameter param) {
        DateFormat formater = null;
        String strFormat = param.getFormat();
        if (strFormat != null && strFormat.trim().length() != 0) {
            formater = new SimpleDateFormat(strFormat);
        } else {
            formater = threadLocal.get();
        }

        return formater;
    }

    public DateFormat getDefaultFormater() {
        return threadLocal.get();
    }
}
