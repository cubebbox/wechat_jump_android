package com.cubebox.tiaoyitiao.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TxtUtil {
    private static final String SPECIAL_CHARACTER = "!@#$%^&*()_-";
    private static final String PHONE = "^1[3|4|5|7|8][0-9]\\d{8}$";


    public static String getNum(String str) {

        String reg = "[\u4e00-\u9fa5]";

        Pattern pat = Pattern.compile(reg);

        Matcher mat = pat.matcher(str);

        String repickStr = mat.replaceAll("");

        return repickStr;
    }

    /**
     * 是否为数字类型
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是移动电话号码
     * 只验证了开始的数字和长度
     */
    public static boolean isMobileNum(String mobiles) {
        if (isEmpty(mobiles)) return false;
        Pattern pattern = Pattern.compile(PHONE);
        Matcher matcher = pattern.matcher(mobiles);
        return matcher.matches();
//        if (mobiles.trim().length() == 11 && !mobiles.trim().isEmpty()) {
//            if ("1".equals(mobiles.substring(0, 1))) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
    }

    /**
     * 手机号 星号处理    asterisk（星号）
     */
    public static String asteriskUserPhone(String mobiles) throws NullPointerException, StringIndexOutOfBoundsException {
        if (isEmpty(mobiles) || mobiles.length() < 11) return "";
        return mobiles.substring(0, 3) + "****" + mobiles.substring(7, mobiles.length());
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为整形并返回
     */
    public static boolean isInteger(String in) {
        try {
            Integer.parseInt(in);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为整形并返回
     */
    public static boolean isDouble(String in) {
        try {
            Double.parseDouble(in);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为整形并返回
     *
     * @return 0 转换失败
     */
    public static int getInteger(String in) {
        try {
            int res = Integer.parseInt(in);
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断是否为整形并返回
     *
     * @return 0 转换失败
     */
    public static long getLong(String in) {
        try {
            long res = Long.parseLong(in);
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断是否为float并返回
     *
     * @return -1 转换失败
     */
    public static float getFloat(String in) {
        try {
            float res = Float.parseFloat(in);
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判断是否为double并返回
     *
     * @return -1 转换失败
     */
    public static double getDouble(String in) {
        try {
            double res = Double.parseDouble(in);
            return res;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 半角转换为全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 替换、过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 得到小数点后保留两位
     */
    public static double get2KeepDecimal(double in) {
        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format(in);
        return getDouble(db);
    }

    /**
     * 得到小数点后保留两位(float)
     */
    public static float get2KeepDecimal(float in) {
        DecimalFormat df = new DecimalFormat("0.00");
        String db = df.format(in);
        return getFloat(db);
    }

    /**
     * 得到小数点后保留一位(float)
     */
    public static float get1KeepDecimal(float in) {
        DecimalFormat df = new DecimalFormat("0.0");
        String db = df.format(in);
        return getFloat(db);
    }


    /**
     * double 相减
     * d1 - d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * 将秒数转为   字符串时间模式   "hh:mm:ss"
     *
     * @param time
     * @return
     */
    public static String getTimeMode(long time) {
        if (time < 0) return "00:00:00";

        long hour, min, sec;
        String hourStr, minStr, secStr;

        hour = time / 3600;
        min = time % 3600 / 60;
        sec = time % 3600 % 60;

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = "" + min;
        }
        if (sec < 10) {
            secStr = "0" + sec;
        } else {
            secStr = "" + sec;
        }

        return hourStr + ":" + minStr + ":" + secStr;
    }

    /**
     * 将二进制字符串转换成int数组
     *
     * @param binStr
     */
    private static int[] BinstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[temp.length - i - 1] = temp[i] - 48;
        }
        return result;
    }

    /**
     * 校验字符串是否含有  大小写字母、数字、特殊字符四种格式中至少两种几以上
     * <p>
     * count: 如果str含有以上四种字符以外的特殊字符，则直接返回-1
     *
     * @param str
     * @return
     */
    public static int checkString(String str) {
        int count = 0;
        boolean isDigit = true;
        boolean isLowerCase = true;
        boolean isUpperCase = true;
        boolean isSpecialChaar = true;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                if (isDigit)
                    count++;
                isDigit = false;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                if (isLowerCase)
                    count++;
                isLowerCase = false;
            } else if (Character.isUpperCase(str.charAt(i))) {
                if (isUpperCase)
                    count++;
                isUpperCase = false;
            } else if (SPECIAL_CHARACTER.contains(str.substring(i, i + 1))) {
                if (isSpecialChaar)
                    count++;
                isSpecialChaar = false;
            } else {
                count = -1;
                return count;
            }
        }
        return count;
    }

    // GENERAL_PUNCTUATION 判断中文的"号

    // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号

    // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号

    /**
     * 是否是中文
     *
     * @param c
     * @return
     */

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;

    }

    /**
     * 是否是英文
     *
     * @param c
     * @return
     */
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }
}
