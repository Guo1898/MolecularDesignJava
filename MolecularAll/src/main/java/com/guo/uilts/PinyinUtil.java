package com.guo.uilts;

import net.sourceforge.pinyin4j.PinyinHelper;

public class PinyinUtil {
    public static String toFirstLetter(String chinese) {
        StringBuilder sb = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyinArray != null && pinyinArray.length > 0) {
                String pinyin = pinyinArray[0];
                sb.append(pinyin.charAt(0));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
