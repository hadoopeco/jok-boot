package com.jokls.jok.rpc.t2.definition.parameter;

import com.jokls.jok.rpc.t2.definition.type.complex.JavabeanJokType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:49
 */
public class Javabean2DatasetUtilTest {


    @Test
    public void testEncode(){
        Integer i  = 111;
        JavabeanJokType javabeanType = new JavabeanJokType("String");
        Parameter subtype = new Parameter();
        List params =new ArrayList<>();
        params.add(subtype);
        javabeanType.setSubTypes(params);

        Parameter parameter = new Parameter();
        parameter.setType(javabeanType);

        Javabean2DatasetUtil.encode(i,parameter,"utf-8");
    }
}