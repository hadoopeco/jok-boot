package com.jokls.jok.event;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:13
 */
public interface IEventFactory {
    IEvent getEvent(String var1, int var2);

    IEvent getEventByAlias(String var1, int var2);
}
