package com.jokls.jok.common.util;

import java.io.File;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 16:52
 */
public class FileUtils {
    public FileUtils() {
    }

    public static boolean cleanDirectory(File dir) {
        boolean flag = false;
        if (!dir.exists()) {
            return flag;
        } else if (!dir.isDirectory()) {
            return flag;
        } else {
            String[] tempList = dir.list();
            File temp = null;

            for(int i = 0; i < tempList.length; ++i) {
                String path = dir.getPath();
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }

                if (temp.isFile()) {
                    temp.delete();
                }

                if (temp.isDirectory()) {
                    cleanDirectory(temp);
                    temp.delete();
                    flag = true;
                }
            }

            return flag;
        }
    }
}
