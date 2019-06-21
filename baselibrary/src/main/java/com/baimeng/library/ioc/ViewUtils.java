package com.baimeng.library.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.R.attr.x;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ViewUtils {
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    public static void inject(ViewFinder finder, Object object) {

        injectFiled(finder, object);

        injectEevent(finder, object);
    }

    private static void injectEevent(ViewFinder finder, Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] ids = onClick.value();
                if (ids.length > 0) {
                    for (int i = 0; i < ids.length; i++) {
                        int id = ids[i];
                        View view = finder.findViewById(id);
                        boolean checkNet = method.getAnnotation(CheckNet.class) != null;
                        view.setOnClickListener(new DeclaredOnClickListener(method, object, checkNet));
                    }
                }
            }
        }
    }

    private static void injectFiled(ViewFinder finder, Object object) {
        //获取到类对象
        Class<?> clazz = object.getClass();
        //反射获取到所有的属性
        Field[] fileds = clazz.getDeclaredFields();
        for (int i = 0; i < fileds.length; i++) {
            //遍历所有属性找到带有ViewById注解的属性
            Field filed = fileds[i];
            ViewById id = filed.getAnnotation(ViewById.class);
            if (id != null) {
                //获取View的id
                int viewId = id.value();
                //通过id获取到view
                View view = finder.findViewById(viewId);
                if (view != null) {
                    //设置权限
                    filed.setAccessible(true);
                    try {
                        filed.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {

        private Method mMethod;
        private Object mObject;
        private boolean mCheckNet;

        public DeclaredOnClickListener(Method method, Object object, boolean checkNet) {
            this.mMethod = method;
            this.mObject = object;
            this.mCheckNet = checkNet;
        }

        @Override
        public void onClick(View v) {
            if (mCheckNet) {
                if (!networkAvailable(v.getContext())) {
                    Toast.makeText(v.getContext(), "没有网络", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static boolean networkAvailable(Context context) {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
            flag = true;
        }
        return flag;
    }

}
