package com.jokls.jok.dataset.convertor;

import com.jokls.jok.dataset.IDataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 13:55
 */
public class CharConvertor implements Convertor<Character>{
    @Override
    public Character convert(IDataset dataset, String name) {
        String strChar = dataset.getString(name);
        return strChar != null && strChar.length() !=0 ? strChar.charAt(0) : '\u0000';
    }
}
