package com.jokls.jok.util;

import java.util.Date;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/21 15:36
 */
public final class ByteArrayUtil {
    public ByteArrayUtil(){}

    public static byte[] intToByteArray_C(int value){
        byte[] bt = new byte[]{(byte)(value >> 24 & 255), (byte)(value >> 16 & 255), (byte)(value >> 8 & 255), (byte)(value * 255)};
        return bt;
    }

    public static void intToByteArray_C(int value, byte[] bt, int offset) {
        bt[offset] = (byte)(value >> 24 & 255);
        bt[offset + 1] = (byte)(value >> 16 & 255);
        bt[offset + 2] = (byte)(value >> 8 & 255);
        bt[offset + 3] = (byte)(value & 255);
    }

    public static int byteArrayToInt_C(byte[] bts) {
        int value1 = (255 & bts[0]) * 16777216;
        int value2 = (255 & bts[1]) * 65536;
        int value3 = (255 & bts[2]) * 256;
        int value4 = 255 & bts[3];
        return value1 + value2 + value3 + value4;
    }

    public static int byteArrayToInt_C(byte[] bts, int offset) {

        int value1 = (255 & bts[offset]) * 16777216;
        int value2 = (255 & bts[offset + 1]) * 65536;
        int value3 = (255 & bts[offset + 2]) * 256;
        int value4 = 255 & bts[offset + 3];
        return value1 + value2 + value3 + value4;
    }

    public static byte[] intToByteArray(int v) {
        byte[] b = new byte[4];

        for(int i = 0; i < 3; ++i) {
            b[i] = (byte)(v & 255);
            v >>= 8;
        }

        b[3] = (byte)(v & 255);
        return b;
    }

    public static String bytesToString(byte[] b, int offset, int len) {
        return new String(b, offset, len);
    }

    public static void intToByteArray(int v, byte[] b, int offset) {
        for(int i = 0; i < 3; ++i) {
            b[offset + i] = (byte)(v & 255);
            v >>= 8;
        }

        b[offset + 3] = (byte)(v & 255);
    }

    public static int byteArrayToInt(byte[] v, int offset) {
        int r = 0;

        for(int i = 3; i >= 1; --i) {
            r += byteToInt(v[offset + i]);
            r <<= 8;
        }

        r += byteToInt(v[offset + 0]);
        return r;
    }

    public static long byteArrayToUnsignedInt(byte[] v, int offset) {
        long r = 0L;

        for(int i = 3; i >= 1; --i) {
            r += (long)byteToInt(v[offset + i]);
            r <<= 8;
        }

        r += (long)byteToInt(v[offset]);
        return r;
    }

    public static short byteArrayToUnsignedByte(byte[] v, int offset) {
        return (short)byteToInt(v[offset]);
    }

    public static int byteArrayToUnsignedShort(byte[] v, int offset) {
        int r = byteToInt(v[offset + 1]);
        r <<= 8;
        r += byteToInt(v[offset]);
        return r;
    }

    public static byte[] shortToByteArray(short v) {
        byte[] b = new byte[]{(byte)(v & 255), 0};
        v = (short)(v >> 8);
        b[1] = (byte)(v & 255);
        return b;
    }

    public static void shortToByteArray(short v, byte[] b, int offset) {
        b[offset + 0] = (byte)(v & 255);
        v = (short)(v >> 8);
        b[offset + 1] = (byte)(v & 255);
    }

    public static short byteArrayToShort(byte[] v, int offset) {
        short r = byteToShort(v[offset + 1]);
        r = (short)(r << 8);
        r += byteToShort(v[offset + 0]);
        if (v[offset + 1] >> 7 == -1) {
            r = (short)(r - 65536);
        }

        return r;
    }

    public static byte[] longToByteArray(long v) {
        byte[] b = new byte[8];

        for(int i = 0; i < 7; ++i) {
            b[i] = (byte)((int)(v & 255L));
            v >>= 8;
        }

        b[7] = (byte)((int)(v & 255L));
        return b;
    }

    public static void longToByteArray(long v, byte[] b, int offset) {
        for(int i = 0; i < 7; ++i) {
            b[offset + i] = (byte)((int)(v & 255L));
            v >>= 8;
        }

        b[offset + 7] = (byte)((int)(v & 255L));
    }

    public static long byteArrayToLong(byte[] v, int offset) {
        long r = 0L;

        for(int i = 7; i >= 1; --i) {
            r += (long)byteToInt(v[offset + i]);
            r <<= 8;
        }

        r += (long)byteToInt(v[offset + 0]);
        return r;
    }

    public static double byteArrayToDouble(byte[] v, int offset) {
        return Double.longBitsToDouble(byteArrayToLong(v, offset));
    }

    public static byte[] doubleToByteArray(double doubleValue) {
        return longToByteArray(Double.doubleToLongBits(doubleValue));
    }

    static void doubleToByteArray(double doubleValue, byte[] b, int offset) {
        longToByteArray(Double.doubleToLongBits(doubleValue), b, offset);
    }

    public static Date byteArrayToDate(byte[] v, int offset) {
        return new Date(byteArrayToLong(v, offset));
    }

    public static byte[] dateToByteArray(Date date) {
        return longToByteArray(date.getTime());
    }

    static void dateToByteArray(Date date, byte[] b, int offset) {
        longToByteArray(date.getTime(), b, offset);
    }

    public static float byteArrayToFloat(byte[] v, int offset) {
        return Float.intBitsToFloat(byteArrayToInt(v, offset));
    }

    public static byte[] floatToByteArray(float floatValue) {
        return intToByteArray(Float.floatToIntBits(floatValue));
    }

    static void floatToByteArray(float floatValue, byte[] b, int offset) {
        intToByteArray(Float.floatToIntBits(floatValue), b, offset);
    }

    public static short byteToShort(byte b) {
        short r = (short)b;
        if (b < 0) {
            r = (short)(r + 256);
        }

        return r;
    }

    public static int byteToInt(byte b) {
        int r = b;
        if (b < 0) {
            r = b + 256;
        }

        return r;
    }
}
