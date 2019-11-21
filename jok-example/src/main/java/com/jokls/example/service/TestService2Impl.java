package com.jokls.example.service;

import com.jokls.jok.rpc.annotation.CloudComponent;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/11/20 21:59
 */

@CloudComponent
public class TestService2Impl implements ITestService2 {
    @Override
    public String test() {
        return "TestServive2 Impl";
    }
}
