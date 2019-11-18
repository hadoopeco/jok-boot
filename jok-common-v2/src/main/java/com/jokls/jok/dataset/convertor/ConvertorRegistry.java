package com.jokls.jok.dataset.convertor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 16:47
 */
public class ConvertorRegistry {
    private static Map<Class<?>, Convertor<?>> innerConverts = new HashMap();
    private Map<Class<?>, Convertor<?>> convertors;

    public ConvertorRegistry() {
        this.convertors = new HashMap();
        this.convertors.putAll(innerConverts);
    }

    public ConvertorRegistry(ConvertorRegistry other) {
        this();
        this.convertors.putAll(other.convertors);
    }

    public <T> Convertor<?> registerConvertor(Class<T> clz, Convertor<?> convertor) {
        return (Convertor)this.convertors.put(clz, convertor);
    }

    public <T> Convertor<?> getConvertor(Class<T> clz) {
        Convertor<?> convertor = this.convertors.get(clz);
        return (Convertor)(convertor == null ? new DefaultConvertor(clz) : convertor);
    }

    static {
        innerConverts.put(Integer.TYPE, new IntConvertor());
        innerConverts.put(Integer.class, new IntConvertor());
        innerConverts.put(Long.TYPE, new LongConvertor());
        innerConverts.put(Long.class, new LongConvertor());
        innerConverts.put(Short.TYPE, new ShortConvertor());
        innerConverts.put(Short.class, new ShortConvertor());
        innerConverts.put(Byte.TYPE, new ByteConvertor());
        innerConverts.put(Byte.class, new ByteConvertor());
        innerConverts.put(Boolean.TYPE, new BooleanConvertor());
        innerConverts.put(Boolean.class, new BooleanConvertor());
        innerConverts.put(Character.TYPE, new CharConvertor());
        innerConverts.put(Character.class, new CharConvertor());
        innerConverts.put(Double.TYPE, new DoubleConvertor());
        innerConverts.put(Double.class, new DoubleConvertor());
        innerConverts.put(Float.TYPE, new FloatConvertor());
        innerConverts.put(Float.class, new FloatConvertor());
        innerConverts.put(String.class, new StringConvertor());
        innerConverts.put(byte[].class, new ByteArrayConvertor());
        innerConverts.put(String[].class, new StringArrayConvertor());
        innerConverts.put(Date.class, new DateConvertor());
        innerConverts.put(BigDecimal.class, new BigDecimalConvertor());
        innerConverts.put(AtomicInteger.class, new AtomicIntegerConvertor());
        innerConverts.put(AtomicLong.class, new AtomicLongConvertor());
    }
}
