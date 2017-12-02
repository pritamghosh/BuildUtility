package com.project.util;

/**
 * <pre>
 * <b>Description : </b>
 * StringUtils.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:18:11 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class StringUtils {
    
    private StringUtils() {
        super();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isEqualsIgnoreCase(String str1, String str2) {
        if (str1 != null) {
            return str1.equalsIgnoreCase(str2);
        }
        return false;
    }

    public static boolean contains(String str1, String str2) {
        if (str1 != null) {
            return str1.contains(str2);
        }
        return false;
    }

    public static String substring(String str, int startIndex) {
        return substring(str, startIndex, -1);
    }

    public static String substring(String str, int startIndexParam, int endIndexParam) {
        if (str != null) {
            int startIndex = startIndexParam;
            int endIndex = endIndexParam;
            if (startIndex < 0)
                startIndex = 0;
            if (endIndex < 0 || endIndex < startIndex)
                endIndex = str.length();
            if (str.length() < startIndex)
                return "";
            else if (str.length() < endIndex)
                return str.substring(startIndex);
            return str.substring(startIndex, endIndex);
        }
        return "";

    }

    public static String replace(String str, String oldStr, String newStr) {
        if (str != null) {
            return str.replace(oldStr, newStr);
        }
        return null;
    }
}
