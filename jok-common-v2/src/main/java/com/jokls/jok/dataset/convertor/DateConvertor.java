package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:57
 */
public class DateConvertor implements Convertor<Date> {

    @Override
    public Date convert(IDataset dataset, String name) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString  = dataset.getString(name);
        try{
            return format.parse(dateString);
        }catch (ParseException e){
            return new Date(0L);
        }
    }
}
