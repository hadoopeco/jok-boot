package com.jokls.jok.util;

import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:16
 */
public class IndexMap<T> {
    private List<T> values = new ArrayList();
    private Map<String, Integer> mapping = new HashMap();

    public T putAndReturn(String name, T t){
        Integer index = this.mapping.get(name);
        if(index != null){
            return this.values.set(index, t);
        }else{
            this.values.add(t);
            this.mapping.put(name, this.values.size() - 1);
            return  null;
        }
    }

    public void put(String name, T t){
        Integer index = this.mapping.get(name);
        if(index != null){
            this.values.set(index, t);
        }else{
            this.values.add(t);
            this.mapping.put(name, this.values.size() -1 );
        }
    }

    public T remove(String name) {
        Integer index = this.mapping.remove(name);
        if (index != null) {
            T v = this.values.get(index);
            this.values.remove(index);
            this.mapping.entrySet().stream().filter(entry -> entry.getValue() > index).forEach(entry -> {
                this.mapping.put(entry.getKey(), entry.getValue() - 1);
            });
            return v;
        } else {
            return null;
        }
    }
    public T get(String name) {
        Integer index = (Integer)this.mapping.get(name);
        return index != null ? this.values.get(index) : null;
    }

    public T get(int index) {
        return this.values.get(index);
    }

    public int getNameIndex(String name) {
        Integer index = (Integer)this.mapping.get(name);
        return index != null ? index : -1;
    }

    public int size() {
        return this.mapping.size();
    }

    public void clear() {
        this.values.clear();
        this.mapping.clear();
    }

    public List<T> getValues() {
        return this.values;
    }

    public Map<String, Integer> getMapping() {
        return this.mapping;
    }
}
