package com.jokls.jok.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:08
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CloudComponent {
    Class<?>[] filters() default {};

}
