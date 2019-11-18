package com.jokls.jok.boot;

import com.jokls.jok.common.boot.CloudApplication;
import com.jokls.jok.common.boot.CloudBootstrap;
import com.jokls.jok.common.web.CloudServletInitializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/7/1 11:32
 */
@CloudApplication(scanBasePackages = {"com.jokls.fsdpl"})
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class}
)
//@EnableCloudDataSource
public class RunApplication extends CloudServletInitializer{
    public static void main(String[] args) {
        try {
            CloudBootstrap.run(RunApplication.class, args);
        } catch (Exception e) {
            System.out.println("RunApplication.main " + e.getMessage());
        }

    }
}
