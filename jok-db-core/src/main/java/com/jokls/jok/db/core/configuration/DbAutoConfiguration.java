package com.jokls.jok.db.core.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.jokls.jok.db.core.dynamic.DynamicDataSourceAspect;
import com.jokls.jok.db.core.dynamic.DynamicDataSourceAspectForClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 15:01
 */
@Configuration
@EnableTransactionManagement
@Order(-2147483647)
public class DbAutoConfiguration {

    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }

    @Bean
    public DynamicDataSourceAspectForClass dynamicDataSourceAspectForClass(){
        return new DynamicDataSourceAspectForClass();
    }

    @Bean
    public ServletRegistrationBean druidStatView(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean druidWebStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter(), new ServletRegistrationBean[0]);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.jsp,/druid/*,/download/*");
        filterRegistrationBean.addInitParameter("sessionStatMaxCount", "2000");
        filterRegistrationBean.addInitParameter("sessionStatEnable", "true");
        filterRegistrationBean.addInitParameter("principalSessionName", "session_user_key");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

}
