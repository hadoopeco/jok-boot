package com.jokls.jok.common.web;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/11/15 15:50
 */

@ExtendWith(OutputCaptureExtension.class)
public class CloudServletInitializerTest {


    private ServletContext servletContext = new MockServletContext();




    static class CustomSpringApplicationBuilder extends SpringApplicationBuilder {


        private boolean built;

        @Override
        public SpringApplication build() {
            this.built = true;
            return super.build();
        }


    }


    private final CustomSpringApplicationBuilder applicationBuilder = new CustomSpringApplicationBuilder();


    @Configuration(proxyBeanMethods = false)
    static class ExecutableWar extends SpringBootServletInitializer {

        @Bean
        ServletWebServerFactory webServerFactory() {
            return new TomcatServletWebServerFactory(8080);
        }

    }

    private SpringApplication application = new SpringApplication(ExecutableWar.class);




    @Test
    public void configure() {
        CloudServletInitializer csi = new CloudServletInitializer();
        SpringApplicationBuilder configure = csi.configure(applicationBuilder);
        Assert.notNull(configure,"configure should be null");
    }



    @Test
    public void run() {
        CloudServletInitializer csi = new CloudServletInitializer();
        WebApplicationContext context = csi.run(application);
        ServletContext servletContext = context.getServletContext();
        Assert.notNull(servletContext,"configure should be null");
    }

}