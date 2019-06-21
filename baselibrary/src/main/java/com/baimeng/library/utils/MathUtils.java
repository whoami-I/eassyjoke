package com.baimeng.library.utils;

/**
 * Created by BaiMeng on 2017/3/31.
 */
public class MathUtils {
    //保留小数点后两位
    public static String getTwo(double i) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(i);
    }
}
