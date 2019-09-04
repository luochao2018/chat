package com.web.chat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作
 */
public class StringUtil<T> {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern upperPattern = Pattern.compile("[A-Z]");
    private static Pattern lowerPattern = Pattern.compile("[a-z]");
    public static final int SUBTRACT = 0;//减
    public static final int ADD = 1;//加
    public static final int MULTIPLY = 2;//乘
    public static final int DIVIDE = 3;//除

    /**
     * 判断为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        String str = String.valueOf(o);
        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 判断不为空
     *
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 转小写
     *
     * @param o
     * @return
     */
    public static String lowerCase(Object o) {
        return formatCase(o, 0);
    }

    /**
     * 转大写
     *
     * @param o
     * @return
     */
    public static String upperCase(Object o) {
        return formatCase(o, 1);
    }

    /**
     * 大小写格式化
     *
     * @param o
     * @param i
     * @return
     */
    private static String formatCase(Object o, Integer i) {
        if (o == null) {
            return "";
        }
        return i == 0 ? o.toString().toLowerCase() : o.toString().toUpperCase();
    }

    /**
     * 返回非null字符串
     *
     * @param o
     * @return
     */
    public static String notNull(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param o           对象
     * @param smallHump   小驼峰
     * @param formatCheck 是否需要校验
     * @return
     */
    public static String lineToHump(Object o, boolean smallHump, boolean formatCheck) {
        if (o == null) {
            return null;
        }
        //校验下划线格式
        if (!formatCheck(o, false, formatCheck)) {
            return o.toString();
        }
        String str = o.toString();
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String lineToHump(Object o) {
        return lineToHump(o, true, true);
    }

    /**
     * 驼峰转下划线
     *
     * @param o           对象
     * @param upperCase   是否转大写
     * @param formatCheck 是否校验
     * @return
     */
    public static String humpToLine(Object o, boolean upperCase, boolean formatCheck) {
        if (o == null) {
            return null;
        }
        //校验驼峰
        if (!formatCheck(o, true, formatCheck)) {
            return o.toString();
        }
        String str = o.toString();
        Matcher matcher = upperPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return upperCase ? sb.toString().toUpperCase() : sb.toString();
    }

    public static String humpToLine(Object o) {
        return humpToLine(o, true, true);
    }

    /**
     * 校验是否为指定格式
     *
     * @param o           对象
     * @param isHump      驼峰
     * @param formatCheck 是否需要校验
     * @return
     */
    public static boolean formatCheck(Object o, boolean isHump, boolean formatCheck) {
        if (!formatCheck) {
            return true;
        }
        if (isEmpty(o)) {
            return false;
        }
        Matcher matcher = null;
        if (isHump) {
            //驼峰
            matcher = linePattern.matcher(o.toString());
            while (matcher.find()) {
                return false;
            }
            matcher = lowerPattern.matcher(o.toString());
            while (matcher.find()) {
                return true;
            }
        } else {
            //下划线格式
            boolean uppLpw = false;
            matcher = upperPattern.matcher(o.toString());
            while (matcher.find()) {
                uppLpw = true;
                break;
            }
            matcher = lowerPattern.matcher(o.toString());
            while (matcher.find()) {
                uppLpw = uppLpw ^ true;
                break;
            }
            return uppLpw;
        }
        return true;
    }

    /**
     * 去掉所有空格
     *
     * @param o
     * @return
     */
    public static String trimAll(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString().replaceAll(" ", "");
    }

    /**
     * 计算
     *
     * @param type
     * @param objects
     * @return
     */
    public static Object calObjects(int type, Object[] objects) {
        float sum = 0f;
        if (objects == null) {
            return null;
        }
        if (objects.length < 2) {
            return objects[0];
        }
        switch (type) {
            case SUBTRACT://减法
                for (Object i : objects) {
                    if (isEmpty(i)) {
                        continue;
                    }
                    sum -= Float.parseFloat(i.toString());
                }
                break;
            case ADD://加法
                for (Object i : objects) {
                    if (isEmpty(i)) {
                        continue;
                    }
                    sum += Float.parseFloat(i.toString());
                }
                break;
            case MULTIPLY://乘法
                for (Object i : objects) {
                    if (isEmpty(i)) {
                        continue;
                    }
                    sum *= Float.parseFloat(i.toString());
                }
                break;
            case DIVIDE://除法
                for (Object i : objects) {
                    if (isEmpty(i)) {
                        continue;
                    }
                    if (Float.parseFloat(i.toString()) == 0) {
                        continue;
                    }
                    sum /= Float.parseFloat(i.toString());
                }
                break;
            default:
                break;
        }
        return String.valueOf(sum);
    }

    /**
     * 计算
     *
     * @param type
     * @param objects
     * @return
     */
    public static Object calObject(int type, Object... objects) {
        return calObjects(type, objects);
    }
}
