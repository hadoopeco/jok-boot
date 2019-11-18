package com.jokls.jok.rpc.async;

import com.jokls.jok.common.trace.TraceInfo;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:47
 */
public interface AsyncFuture {
    void set(Object value);

    void set(Throwable exception);

    TraceInfo getTrace();
}
