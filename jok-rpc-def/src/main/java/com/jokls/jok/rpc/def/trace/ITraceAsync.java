package com.jokls.jok.rpc.def.trace;


import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 14:19
 */
public interface ITraceAsync{
    void logConsumerAsync(TraceInfo newTrace, String depotId, BaseException baseException, byte[] msgBody, String charset);

    void logConsumer(TraceInfo newTrace, Invoker<?> invoker, Invocation invocation, String depotId, BaseException baseException, boolean isAsync, byte[] msgBody);

    void logProvider(TraceInfo newTrace, Invoker<?> invoker, Invocation invocation, BaseException baseException);
}
