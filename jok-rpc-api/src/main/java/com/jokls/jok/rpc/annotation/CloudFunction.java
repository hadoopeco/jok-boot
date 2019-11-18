package com.jokls.jok.rpc.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:11
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CloudFunction {
    @AliasFor("functionId")
    String value() default "";

    @AliasFor("value")
    String functionId() default "";

    String serviceId() default "";

    String type() default "";

    String dec() default "";

    boolean async() default false;

    boolean openApi() default true;
}
