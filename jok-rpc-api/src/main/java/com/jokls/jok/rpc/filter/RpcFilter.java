package com.jokls.jok.rpc.filter;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 16:03
 */
public interface RpcFilter {

    boolean needToLoad();

    void before(FilterContext context);

    void after(Object obj, FilterContext context);
}
