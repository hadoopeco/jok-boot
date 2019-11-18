package com.jokls.jok.common.config;

import com.jokls.jok.common.util.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 15:59
 */
@Configuration
@Order(-2147483648)
public class CloudAutoConfiguration {
    public CloudAutoConfiguration() {
    }

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
