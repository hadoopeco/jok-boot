package com.jokls.jok.db.core.configuration;

import com.jokls.jok.db.def.dynamic.DynamicDataSourceRegister;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/7/9 10:45
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableTransactionManagement
@Import({DbAutoConfiguration.class, DynamicDataSourceRegister.class})
public @interface EnableCloudDataSource {
}
