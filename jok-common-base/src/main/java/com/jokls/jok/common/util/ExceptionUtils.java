package com.jokls.jok.common.util;

import com.jokls.jok.common.exception.BaseException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 15:17
 */
public class ExceptionUtils {
    public ExceptionUtils() {
    }

    public static String getErrorCode(Throwable e, int defaultCode) {
        return e instanceof BaseException ? ((BaseException)e).getErrorCode() : defaultCode + "";
    }

    public static BaseException getBaseException(Throwable e, int defaultCode) {
        if (e instanceof BaseException) {
            return (BaseException)e;
        } else {
            BaseException be = new BaseException(defaultCode + "", e);
            if (!StringUtils.isEmpty(e.getMessage())) {
                be.setErrorMessage(e.getMessage());
            } else {
                be.setErrorMessage(e.getClass().getName());
            }

            return be;
        }
    }
}
