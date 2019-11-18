package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

import java.math.BigDecimal;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:11
 */
public class BigDecimalConvertor implements Convertor<BigDecimal> {
    @Override
    public BigDecimal convert(IDataset dataset, String name) {
        String number = dataset.getString(name);

        try{
            return new BigDecimal(number);
        }catch (Exception e){
            return null;
        }
    }
}
