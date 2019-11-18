package com.jokls.jok.rpc.t2.base;

import com.jokls.jok.rpc.t2.definition.type.JokType;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/26 15:27
 */
public interface TypeDefinitionContainer {
    JokType getType(String typeName);

    void addType(JokType type);
}
