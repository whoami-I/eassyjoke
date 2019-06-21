package com.baimeng.library.utils;

/**
 * Created by BaiMeng on 2017/3/28.
 */
public class CommonUtils {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1200) {
            return true;

        }
        lastClickTime = time;
        return false;
    }

    public static boolean isLongTimeClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 10000) {
            return true;

        }
        lastClickTime = time;
        return false;
    }
}
