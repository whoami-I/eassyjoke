package com.baimeng.library.utils;

import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * Created by BaiMeng on 2017/3/1.
 */
public class UiUtils {
    public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
