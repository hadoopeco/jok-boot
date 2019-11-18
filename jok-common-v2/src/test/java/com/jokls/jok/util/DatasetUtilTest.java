package com.jokls.jok.util;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:17
 */
public class DatasetUtilTest {


    @Test
    public void testPack(){
        IDataset ds = DatasetService.getDefaultInstance().getDataset();
        byte[] rs = DatasetUtil.pack(ds,"UTF-8");
        System.out.println( " ssss " + new String(rs));
    }

}