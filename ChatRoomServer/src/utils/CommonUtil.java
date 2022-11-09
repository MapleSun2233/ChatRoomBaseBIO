package utils;

import constant.CommonConstant;

/**
 * @author Maple
 */
public class CommonUtil {
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || CommonConstant.EMPTY_STRING.equals(str);
    }

    public static boolean isFalse(boolean flag) {
        return !flag;
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }

    public static boolean isEquals(Object o1, Object o2) {
        return isNotNull(o1) && isNotNull(o2) && o1.equals(o2);
    }

    public static String deleteAllBlankChar(String origin) {
        return origin.replaceAll(CommonConstant.BLANK_CHAR_REGEX, CommonConstant.EMPTY_STRING);
    }
}
