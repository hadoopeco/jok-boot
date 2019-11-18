package com.jokls.jok.rpc.def.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 18:15
 */
public class RpcUtilsTest {

    @Test
    public void postMockServer(){
        String rs = RpcUtils.postMockServer("http://dev-www.hljinke.com:9000","{t:tt}");
        System.out.printf(""+rs);
    }

}