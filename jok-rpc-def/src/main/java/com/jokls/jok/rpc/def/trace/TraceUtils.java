package com.jokls.jok.rpc.def.trace;

import com.jokls.jok.common.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TraceUtils {
    private static final Logger logger = LoggerFactory.getLogger(TraceUtils.class);
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String generatorTraceId() {
        StringBuffer buff = new StringBuffer();
        buff.append(UUID.randomUUID().toString().replaceAll("-", ""));
        buff.append(longToHex(RandomUtils.nextLong()));
        return buff.toString();
    }


    public static String longToHex(long id) {
        char[] data = new char[16];
        writeHexLong(data, 0, id);
        return new String(data);
    }

    private static void writeHexLong(char[] data, int pos, long v) {
        writeHexByte(data, pos + 0, (byte)((int)(v >>> 56 & 255L)));
        writeHexByte(data, pos + 2, (byte)((int)(v >>> 48 & 255L)));
        writeHexByte(data, pos + 4, (byte)((int)(v >>> 40 & 255L)));
        writeHexByte(data, pos + 6, (byte)((int)(v >>> 32 & 255L)));
        writeHexByte(data, pos + 8, (byte)((int)(v >>> 24 & 255L)));
        writeHexByte(data, pos + 10, (byte)((int)(v >>> 16 & 255L)));
        writeHexByte(data, pos + 12, (byte)((int)(v >>> 8 & 255L)));
        writeHexByte(data, pos + 14, (byte)((int)(v & 255L)));
    }

    private static void writeHexByte(char[] data, int pos, byte b) {
        data[pos + 0] = HEX_DIGITS[b >> 4 & 15];
        data[pos + 1] = HEX_DIGITS[b & 15];
    }
}
