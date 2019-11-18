package com.jokls.jok.common.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;


/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/11/15 15:11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CloudContextLoaderListernerTest.class)
public class CloudContextLoaderListernerTest {

    private ServletContext servletContext = Mockito.mock(ServletContext .class);



    //测试   web Application Context init
    @Test
    public void initWebApplicationContext() {
        Mockito.when(servletContext.getInitParameterNames()).thenReturn(Collections.<String>emptyEnumeration());
        Mockito.when(servletContext.getAttributeNames()).thenReturn(Collections.<String>emptyEnumeration());
        Mockito.when(servletContext.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM))
                .thenReturn(TestConfiguration.class.getName());
        CloudContextLoaderListerner listener = new CloudContextLoaderListerner();
        assertNotNull(listener.initWebApplicationContext(servletContext));
    }
}