package com.jokls.jok.common.util;

import java.util.Date;
import java.util.Random;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:46
 */
public class RandomUtils {
    private RandomUtils() {
    }

    public static Boolean nextBoolean() {
        Random rd = new Random();
        return rd.nextDouble() > 0.5D;
    }

    public static Byte nextByte() {
        Random rd = new Random();
        return Byte.valueOf(("" + rd.nextDouble() * 2.147483647E9D).substring(0, 1));
    }

    public static Integer nextInteger() {
        Random rd = new Random();
        return rd.nextInt();
    }

    public static Character nextCharacter() {
        int end = 123;
        int start = 32;
        StringBuilder buffer = new StringBuilder();
        int gap = end - start;
        Random rd = new Random();
        buffer.append((char)(rd.nextInt() * gap + start));
        return buffer.toString().charAt(0);
    }

    public static String nextString(Integer length) {
        int end = 123;
        int start = 32;
        StringBuilder buffer = new StringBuilder();
        int gap = end - start;
        Random rd = new Random();

        for(int i = 0; i < length; ++i) {
            char ch = (char)(rd.nextInt() * gap + start);
            if (Character.isLetter(ch)) {
                buffer.append(ch);
            } else {
                --i;
            }
        }

        return buffer.toString();
    }

    public static Long nextLong() {
        Random rd = new Random();
        return (long)(rd.nextDouble() * 9.223372036854776E18D);
    }

    public static Date nextDate() {
        long date = nextLong();
        long maxDate = (new Date()).getTime();
        return new Date(date > maxDate ? maxDate : date);
    }

    public static Short nextShort() {
        Random rd = new Random();
        return (short)(rd.nextInt() * 32767);
    }

    public static Double nextDouble() {
        Random rd = new Random();
        return rd.nextDouble() * 1.7976931348623157E308D;
    }
}
