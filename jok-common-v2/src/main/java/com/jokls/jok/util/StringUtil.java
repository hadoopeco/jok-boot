package com.jokls.jok.util;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:07
 */
public class StringUtil {

    public static int toInt(String str){
        if(str != null && str.length() != 0){
            int result = 1;
            int size = str.length();
            for(int i = 0; i < size; ++i) {
                char ch = str.charAt(i);
                if (ch < '0' || ch > '9') {
                    return -1;
                }

                if (i == 0) {
                    result = ch - 48;
                } else {
                    result = result * 10 + (ch - 48);
                }
            }

            return result;

        }else {
            return -1;
        }
    }


    public static int toInt(String str, int def) {
        if (str != null && str.length() != 0) {
            int result = 1;
            int size = str.length();

            for(int i = 0; i < size; ++i) {
                char ch = str.charAt(i);
                if (ch < '0' || ch > '9') {
                    return def;
                }

                if (i == 0) {
                    result = ch - 48;
                } else {
                    result = result * 10 + (ch - 48);
                }
            }

            return result;
        } else {
            return def;
        }
    }

    public static int toIntWithException(String str, int def) throws Exception {
        if (str != null && str.length() != 0) {
            int result = 1;
            int size = str.length();

            for(int i = 0; i < size; ++i) {
                char ch = str.charAt(i);
                if (ch < '0' || ch > '9') {
                    throw new Exception();
                }

                if (i == 0) {
                    result = ch - 48;
                } else {
                    result = result * 10 + (ch - 48);
                }
            }

            return result;
        } else {
            throw new Exception();
        }
    }

    public static final boolean equals(String oper1, String oper2) {
        if (oper1 == oper2) {
            return true;
        } else if (oper1 != null && oper2 != null) {
            if (oper1.length() != oper2.length()) {
                return false;
            } else {
                return oper1.compareTo(oper2) == 0;
            }
        } else {
            return false;
        }
    }

}
