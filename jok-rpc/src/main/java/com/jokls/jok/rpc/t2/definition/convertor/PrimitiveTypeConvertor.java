package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.exception.ParseParamsException;
import com.jokls.jok.rpc.t2.definition.parameter.Parameter;
import com.jokls.jok.rpc.t2.definition.type.JokType;
import com.jokls.jok.util.DatasetUtil;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 16:51
 */
public class PrimitiveTypeConvertor implements TypeConvertor {
    private Object defaultValue = null;
    private Constructor constructor;
    private Class clazz;
    private static Map<Class, Class> PrimaritiveWrapperMap = new HashMap(8);

    public PrimitiveTypeConvertor(Class clazz, Object defaultValue) {
        this.defaultValue = defaultValue;
        this.clazz = clazz;

        try {
            if (clazz.isPrimitive()) {
                if (clazz == Character.TYPE) {
                    this.constructor = ((Class) PrimaritiveWrapperMap.get(clazz)).getConstructor(Character.TYPE);
                } else {
                    this.constructor = ((Class) PrimaritiveWrapperMap.get(clazz)).getConstructor(String.class);
                }
            } else {
                this.constructor = clazz.getConstructor(String.class);
            }

        } catch (Exception e) {
            throw new ParseParamsException("invalid primitive type:" + clazz, e);
        }
    }

    public Object encode(Object obj, Parameter param, String charset) {
        JokType type = param.getType();
        if (param.isArray()) {
            if (obj == null) {
                return null;
            } else {
                IDataset ds = DatasetService.getDefaultInstance().getDataset();
                ds.addColumn(type.getTypeName(), type.getTransType());
                List array = (List) obj;
                int size = array.size();

                for (int i = 0; i < size; ++i) {
                    ds.appendRow();
                    Object value = array.get(i);
                    if (value != null) {
                        ds.updateString(1, value.toString());
                    }
                }

                return DatasetUtil.pack(ds, charset);
            }
        } else {
            return obj == null ? null : obj.toString();
        }
    }

    public Object decode(Object obj, Parameter param, String charset) {
        if (obj != null && !StringUtils.isEmpty(obj.toString())) {
            try {
                if (!param.isArray()) {
                    return obj instanceof byte[] ? this.decode(new String((byte[]) ((byte[]) obj))) : this.decode(obj.toString());
                } else {
                    byte[] bobj = (byte[]) obj;
                    if (bobj.length == 0) {
                        return null;
                    } else {
                        IDataset ds = DatasetUtil.unpack(bobj, charset);
                        if (ds == null) {
                            return null;
                        } else {
                            List listObj = new ArrayList();
                            int rowCount = ds.getRowCount();

                            for (int i = 1; i <= rowCount; ++i) {
                                ds.locateLine(i);
                                listObj.add(this.decode(ds.getString(1)));
                            }

                            return listObj;
                        }
                    }
                }
            } catch (Exception e) {
                throw new ParseParamsException("failed to paser[" + obj + "] to [" + this.clazz + "]", e);
            }
        } else {
            return null;
        }
    }

    private Object decode(String value) throws Exception {
        try {
            return this.constructor.newInstance(value);
        } catch (Exception e) {
            return null;
        }
    }

    static {
        PrimaritiveWrapperMap.put(Integer.TYPE, Integer.class);
        PrimaritiveWrapperMap.put(Long.TYPE, Long.class);
        PrimaritiveWrapperMap.put(Double.TYPE, Double.class);
        PrimaritiveWrapperMap.put(Float.TYPE, Float.class);
        PrimaritiveWrapperMap.put(Character.TYPE, Character.class);
        PrimaritiveWrapperMap.put(Short.TYPE, Short.class);
        PrimaritiveWrapperMap.put(Byte.TYPE, Byte.class);
        PrimaritiveWrapperMap.put(Boolean.TYPE, Boolean.class);
    }
}
