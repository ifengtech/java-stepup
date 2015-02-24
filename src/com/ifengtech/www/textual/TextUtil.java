package com.ifengtech.www.textual;

import java.util.Random;

/**
 * Created by Wang Gensheng on 2015-01-28.
 */
public class TextUtil {

    public static final String DEBUG_TAG = "TextUtil";
    public static final boolean DEBUG = true;

    public static boolean isEmpty(CharSequence str) {
        if(str == null || str.length() == 0) {
            return true;
        }

        int start = 0;
        while(start < str.length()) {
            if(!isWhiteSpace(str.charAt(start)))
                break;
            start++;
        }

        CharSequence sub = str.subSequence(start, str.length());
        return sub == null;
    }

    public static boolean isWhiteSpace(char ch) {
        if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
            return true;
        else
            return false;
    }

    public static String getFirstLine(String para) {
        if(isEmpty(para))
            return null;

        int le;
        le = (le = para.indexOf('\n')) != -1 ? le : ((le = para.indexOf('\r')) != -1 ? le : -1);

        if(le != -1)
            return para.substring(0, le);
        else
            return para;
    }

    public static boolean textEqual(CharSequence s1, CharSequence s2) {
        return false;
    }

    public static String generate8ByteId() {
        return null;
    }

    public static String composeDangleString(String clause, String[] args) {
        String builder = clause;
        int i;
        if (!isEmpty(builder) && (args != null && args.length != 0)) {
            for (i = 0; i < args.length; i++) {
                builder = builder.replaceFirst("\\?", args[i]);
            }
            if (builder.indexOf('?') != -1) {
                throw new IllegalArgumentException("clause和args参数个数不匹配");
            }
        }
        return builder;
    }

    public static String randomStringCode() {
        final int CODE_LENGTH = 8;
        return randomStringCode(CODE_LENGTH);
    }

    /**
     * 生成指定长度的随机字符串
     * @param length 指定length个字符
     * @return
     */
    public static String randomStringCode(int length) {
        final String SPEC_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123467890";
        if (length <= 0) {
            return null;
        }

        char[] buf = new char[length];
        Random random = new Random();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = SPEC_CHAR.charAt(random.nextInt(SPEC_CHAR.length()));
        }
        return new String(buf);
    }
}
