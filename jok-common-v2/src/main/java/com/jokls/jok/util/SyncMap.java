package com.jokls.jok.util;

import java.util.HashMap;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 11:22
 */
public class SyncMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    public synchronized V get(Object key){return super.get(key);}

    public synchronized V put(K key, V value){return super.put(key, value);}

    public synchronized V remove(Object key){return super.get(key);}
}
