package utils;

import constant.CommonConstant;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommonUtil {
    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || CommonConstant.EMPTY_STRING.equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return isNotNull(str) && isFalse(isEmpty(str));
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

    public static boolean isNotEquals(Object o1, Object o2) {
        return isNull(o1) || isNull(o2) || !o1.equals(o2);
    }

    public static String deleteAllBlankChar(String origin) {
        return origin.replaceAll(CommonConstant.BLANK_CHAR_REGEX, CommonConstant.EMPTY_STRING);
    }

    public static String[] verifyAndGetAddressInfo(String text) {
        String address = deleteAllBlankChar(text);
        if (isEmpty(address) || isFalse(address.contains(CommonConstant.COLON))) {
            return null;
        }
        String[] addressInfo = address.split(CommonConstant.COLON);
        String ip = addressInfo[0];
        int port = Integer.parseInt(addressInfo[1]);
        boolean validIp = false;
        boolean validPort = port >= 0 && port <= 65535;
        // 验证ip地址格式
        if (ip.contains(CommonConstant.DOT)) {
            validIp = Arrays.stream(ip.split(CommonConstant.DOT)).map(Integer::parseInt).allMatch(num -> num >= 0 && num <= 255);
        }
        // 验证是否同时有效
        if (validIp && validPort) {
            return addressInfo;
        } else {
            return null;
        }
    }

    public static boolean isNotMatches(String text, String regex) {
        return isEmpty(text) || isFalse(text.matches(regex));
    }

    public static boolean isZero(int num) {
        return num == 0;
    }

    public static String join(String ...args) {
        return String.join("", args);
    }
}
