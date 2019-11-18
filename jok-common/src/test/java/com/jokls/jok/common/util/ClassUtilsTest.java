package com.jokls.jok.common.util;

import org.junit.Test;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/11/15 16:58
 */
public class ClassUtilsTest {

    @Test
    public void scanInterface() {
        String args[] = {"*", ""};

        ClassUtils.scanInterface(args,null);
    }

    @Test
    public void scanFile() {
    }

    @Test
    public void isWrapClass() {
    }
}