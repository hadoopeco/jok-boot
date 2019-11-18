package com.jokls.jok.rpc.annotation;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:42
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CloudService{
    String group() default "";

    String version() default "";

    boolean openApi() default true;

    int delay() default 0;

    boolean singleMode() default false;

    boolean validation() default false;
}
