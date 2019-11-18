package com.jokls.jok.rpc.t2.util;

import com.jokls.jok.rpc.t2.base.TypeDefinitionContainer;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:47
 */
public class ServiceDefinitionUtilTest {

    @Test
    public void test(){
        TypeDefinitionContainer container = ServiceDefinitionUtil.getTypeDefContainer();
        container.getType("test");

    }

}