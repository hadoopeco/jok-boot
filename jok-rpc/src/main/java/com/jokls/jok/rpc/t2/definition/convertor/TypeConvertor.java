package com.jokls.jok.rpc.t2.definition.convertor;

import com.jokls.jok.rpc.t2.definition.parameter.Parameter;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 9:26
 */
public interface TypeConvertor {
    Object encode(Object obj, Parameter parameter, String charset);

    Object decode(Object obj, Parameter parameter, String charset);
}
