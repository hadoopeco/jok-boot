package com.jokls.jok.context;

import com.jokls.jok.context.impl.CommonServiceContext;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 10:06
 */
public class ContextUtil {
    private static final IServiceContext serviceContext = new CommonServiceContext();

    public ContextUtil() {
    }

    public static IServiceContext getServiceContext() {
        return serviceContext;
    }
}
