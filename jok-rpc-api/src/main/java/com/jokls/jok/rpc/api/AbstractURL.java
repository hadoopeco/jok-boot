package com.jokls.jok.rpc.api;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:44
 */
public abstract class AbstractURL {
    public abstract String getProtocol();

    public abstract String getAddress();

    public abstract String getPath();

    public abstract String getServiceInterface();

    public abstract String getGroup();

    public abstract String getVersion();
}
