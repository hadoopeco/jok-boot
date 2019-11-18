package com.jokls.jok.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:43
 */
public class ErrorFormatter {
    private Properties errorInfos = new Properties();
    private static ErrorFormatter INSTANCE = new ErrorFormatter();

    private ErrorFormatter() {
        InputStream stream = null;

        try {
            stream = ErrorFormatter.class.getResourceAsStream("/sysErrorFormat.properties");
            this.errorInfos.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }

        }

    }

    public static ErrorFormatter getInstance() {
        return INSTANCE;
    }

    public String format(String errorNo, Object... message) {
        String pattern = this.errorInfos.getProperty(errorNo);
        return pattern != null ? MessageFormat.format(pattern, message) : MessageFormat.format("{0}", message);
    }

    public void putErrorMap(Map<String, Object> errorMap) {
        this.errorInfos.putAll(errorMap);
    }
}
