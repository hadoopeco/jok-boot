package com.jokls.jok.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:47
 */
public class DateUtils {
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };
    private static ThreadLocal<DateFormat> threadLocalUTC = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sfd;
        }
    };

    public DateUtils() {
    }

    public static String getDateString(Date date) {
        return ((DateFormat)threadLocal.get()).format(date);
    }

    public static String getDateStringNow() {
        return ((DateFormat)threadLocal.get()).format(new Date());
    }

    public static String getDateStringNowUTC() {
        return ((DateFormat)threadLocalUTC.get()).format(new Date());
    }
}
