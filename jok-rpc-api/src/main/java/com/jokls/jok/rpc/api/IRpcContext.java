package com.jokls.jok.rpc.api;

import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.rpc.async.AsyncFuture;

import java.util.concurrent.Future;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:44
 */
public interface IRpcContext {
    void putRequestBaggage(String key, String value);

    void putResponseBaggage(String key, String value);

    String getRequestBaggage(String key);

    String getResponseBaggage(String key);

    Future getFuture();

    void set(String key, Object value);

    void setTrace(TraceInfo info);

    Object get(String key);

    AsyncFuture getAsyncFuture();
}
