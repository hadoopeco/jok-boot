package com.jokls.jok.context.impl;

import com.jokls.jok.context.IServiceContext;
import com.jokls.jok.event.IEventFactory;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:18
 */
public class CommonServiceContext implements IServiceContext {


    @Override
    public IEventFactory getEventFactory() {
        return null;
    }

    @Override
    public IEventFactory getT3EventFactory() {
        return null;
    }
}
