package com.jokls.jok.rpc.callback;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:58
 */
public interface ResponseCallback {
    void success(Object var1, CallbackContext var2);

    void failure(Throwable var1, CallbackContext var2);
}
