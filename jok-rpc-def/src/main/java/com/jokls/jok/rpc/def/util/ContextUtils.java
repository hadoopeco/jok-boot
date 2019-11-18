package com.jokls.jok.rpc.def.util;

import org.apache.dubbo.rpc.RpcContext;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 17:56
 */
public class ContextUtils {

    public static Object get(String key){
        return RpcContext.getContext().get(key);
    }
}
