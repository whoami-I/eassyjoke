package com.baimeng.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by BaiMeng on 2017/5/16.
 */
public class APPInfo {
    /**
     * 获取软件版本号
     *
     * @return
     */
       /*
     * 获取当前程序的版本号
     */
    public static String getVersionName(Context context) {
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }
}
