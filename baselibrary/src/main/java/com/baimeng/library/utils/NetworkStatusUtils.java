package com.baimeng.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络状态工具类
 * Created by LiuHe on 2017/5/18.
 */

public class NetworkStatusUtils {
    /**
     * wifi或数据连接是否连接
     *
     * @param context
     * @return true : 两者有一个处于连接状态;
     */
    public static boolean checkNetWorkConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !manager.getBackgroundDataSetting())
            return false;
        else
            return true;
    }
}
