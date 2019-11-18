package com.jokls.jok.rpc.def.monitor;

import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 14:14
 */
public interface IMonitorAsync {
    void statConsumer(Invoker<?> invoker, Invocation invocation, String errorCode, Long time);

    void statProvider(Invoker<?> invoker, Invocation invocation, String errorCode, Long time);
}
