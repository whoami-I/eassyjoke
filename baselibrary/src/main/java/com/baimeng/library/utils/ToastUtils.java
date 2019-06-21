package com.baimeng.library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by BaiMeng on 2017/1/16.
 */
public class ToastUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
