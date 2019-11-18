package com.jokls.jok.util;

public class StringMatcher {

    public static boolean isValidString(String value){
        return value != null && value.trim().length() != 0;
    }

    public static boolean matchServiceList(String src, String[] desList){
        int size = desList.length;
        String serviceId = src;

        for(int i=0; i < size; i++){
            if(match(serviceId, desList[i])){
                return true;
            }
        }

        return false;
    }


    public static boolean match(String src, String des){
        if(src != null && des != null && src.trim().length() != 0 && des.trim().length() != 0){
            if("*".equals(des.trim())){
                return true;
            } else {
                boolean beginWithAsterisk = des.startsWith("*");
                boolean endWithAsterisk = des.endsWith("*");
                String[] asteriskSplit = des.split("[*]");

                int srcIndex = 0;
                int aIndex = 0;
                int asteriskSize = asteriskSplit.length;

                boolean ok;
                for(ok = true ; ok && aIndex < asteriskSize; ++aIndex){
                    String aDes = asteriskSplit[aIndex];
                    int index = match(src, aDes, srcIndex);
                    if (index == -1 ){
                        ok = false;
                        break;
                    }

                    if(aIndex == 0 && !beginWithAsterisk && index !=0){
                        ok = false;
                    }

                    srcIndex = index + aDes.length();

                }

                if(ok && !endWithAsterisk && src.length() - srcIndex !=0){
                    ok = false;
                }

                return ok;
            }
        }else{
            return false;
        }
    }


    private static int match(String source, String target, int fromIndex) {
        int sourceCount = source.length();
        int targetCount = target.length();
        if (fromIndex >= sourceCount) {
            return targetCount == 0 ? sourceCount : -1;
        } else {
            if (fromIndex < 0) {
                fromIndex = 0;
            }

            if (fromIndex + targetCount > sourceCount) {
                return -1;
            } else if (targetCount == 0) {
                return fromIndex;
            } else {
                char first = target.charAt(0);
                int max = sourceCount - targetCount;

                for (int i = fromIndex; i <= max; i++) {
                    if (source.charAt(i) != first && first != '?') {
                        do {
                            i++;
                        } while (i <= max && source.charAt(i) != first);
                    }

                    if (i <= max) {
                        int j = i + 1;
                        int end = j + targetCount - 1;

                        for (int k = 1; j < end && (target.charAt(k) == '?' || source.charAt(j) == target.charAt(k)); k++) {
                            ++j;
                        }

                        if (j == end) {
                            return i;
                        }
                    }
                }
                return -1;
            }
        }
    }


}
