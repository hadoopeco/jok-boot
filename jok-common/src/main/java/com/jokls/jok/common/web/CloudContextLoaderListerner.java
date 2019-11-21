package com.jokls.jok.common.web;

import com.jokls.jok.common.boot.CloudBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.legacy.context.web.SpringBootContextLoaderListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 17:29
 */
public class CloudContextLoaderListerner extends SpringBootContextLoaderListener {
    private final Logger logger = LoggerFactory.getLogger(CloudContextLoaderListerner.class);
    private static final String INIT_PARAM_DELIMITERS = ",; \t\n";

    public CloudContextLoaderListerner() {
    }

    //初始化webApplication上下文
    public WebApplicationContext initWebApplicationContext(final ServletContext servletContext) {
        logger.info("initWebApplicationContext start");
        String configLocationParam = servletContext.getInitParameter("contextConfigLocation");


        SpringApplicationBuilder builder = new SpringApplicationBuilder(StringUtils.tokenizeToStringArray(configLocationParam, INIT_PARAM_DELIMITERS).getClass());
        Class contextClass = this.determineContextClass(servletContext);

        builder.contextClass(contextClass);
        builder.initializers((ApplicationContextInitializer<GenericWebApplicationContext>) applicationContext -> applicationContext.setServletContext(servletContext));

        String[] args = null;
        String jfile = servletContext.getInitParameter("jfile");
        if (!StringUtils.isEmpty(jfile)) {
            args = new String[]{"jfile=" + jfile};
        }

        args = CloudBootstrap.prepare(args);
        WebApplicationContext context ;


        if (args != null) {
            context = (WebApplicationContext)builder.run(args);
        } else {
            context = (WebApplicationContext)builder.run(new String[0]);
        }

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        return context;
    }
}
