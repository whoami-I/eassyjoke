package com.baimeng.library.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BaiMeng on 2017/6/28.
 */
public class TextColorUtils {
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        String string = text.toLowerCase();
        String key = keyword.toLowerCase();

        Pattern pattern = Pattern.compile(key);
        Matcher matcher = pattern.matcher(string);

        SpannableString ss = new SpannableString(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public static SpannableString matcherSearchTitleContain(int color, String text, String keyword) {
        String string = text.toLowerCase();
        String key = keyword.toLowerCase();

        Pattern pattern = Pattern.compile(key);
        Matcher matcher = pattern.matcher(string);

        SpannableString ss = new SpannableString(text);
        while (matcher.find()) {
            int start = matcher.start() - 1;
            int end = matcher.end() + 1;
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
}
