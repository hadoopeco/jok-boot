package com.jokls.jok.context;

import com.jokls.jok.event.IEventFactory;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:12
 */
public interface IServiceContext {
    IEventFactory getEventFactory();

    IEventFactory getT3EventFactory();
}
