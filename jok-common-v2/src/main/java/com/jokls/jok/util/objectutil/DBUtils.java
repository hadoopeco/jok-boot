package com.jokls.jok.util.objectutil;

public class DBUtils {

    public static String convertToFieldName(String attributeName) {
        if (attributeName == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            char[] nameChars = attributeName.toCharArray();


            for (char nameChar : nameChars) {
                char c = nameChar;
                if (c >= 'A' && c <= 'Z') {
                    sb.append('_');
                }

                if (c >= 'a' && c <= 'z') {
                    c = (char) (c - 32);
                }

                sb.append(c);
            }

            return sb.toString();
        }
    }
}
