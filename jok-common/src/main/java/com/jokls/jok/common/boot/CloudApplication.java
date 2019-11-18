package com.jokls.jok.common.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:53
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableAsync
public @interface CloudApplication {
    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "exclude"
    )
    Class<?>[]  exclude() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "excludeName"
    )
    String[] excludeName() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};
}
