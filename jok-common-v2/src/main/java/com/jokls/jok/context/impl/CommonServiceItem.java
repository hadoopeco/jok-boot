package com.jokls.jok.context.impl;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 9:32
 */
public class CommonServiceItem {
    private Class<?> clz;

    private Object service;

    public Class<?> getClz() {
        return this.clz;
    }

    public void setClz(Class<?> clz){
        this.clz = clz;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public CommonServiceItem(Object service, Class<?> clz) {
        this.service = service;
        this.clz = clz;
    }
}
