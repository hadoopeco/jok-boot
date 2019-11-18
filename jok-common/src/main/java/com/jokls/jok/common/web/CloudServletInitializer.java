package com.jokls.jok.common.web;

import com.jokls.jok.common.boot.CloudBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 17:39
 */
public class CloudServletInitializer extends SpringBootServletInitializer {
    public CloudServletInitializer() {
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    protected WebApplicationContext run(SpringApplication application) {
        return (WebApplicationContext) CloudBootstrap.run(application);
    }
}
