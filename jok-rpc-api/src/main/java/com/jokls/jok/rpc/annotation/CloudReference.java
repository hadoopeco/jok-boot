package com.jokls.jok.rpc.annotation;

import java.lang.annotation.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:41
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CloudReference {
    String group() default "";

    String version() default "";

    int timeout() default 0;

    String targetUrl() default "";

    String protocol() default "";

    String callType() default "sync";

    String charset() default "";

    Class<?> callbackClass() default Void.class;

    Class<?>[] filters() default {};

    int connections() default 0;

    int retries() default 0;

    boolean check() default false;

    String loadbalance() default "";

    String service() default "";
}
