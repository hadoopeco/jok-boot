package com.jokls.jok.event;

import com.jokls.jok.event.pack.PackV1;
import com.jokls.jok.event.pack.PackV2;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:30
 */
public class PackService {
    public PackService() {
    }

    public static IPack getPacker(int version, String charset) {
        return version == 1 ? new PackV1(charset) : new PackV2(charset);
    }

    public static IPack getPacker(byte[] data, String charset) {
        try {
            return getVersion(data) == 1 ? new PackV1(data, charset) : new PackV2(data, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersion(byte[] data) {
        return data[0] < 48 && data[0] != 32 ? 2 : 1;
    }
}
