package com.baimeng.framelibrary.base;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;


import com.baimeng.framelibrary.skin.SkinAttrSupport;
import com.baimeng.framelibrary.skin.SkinManager;
import com.baimeng.framelibrary.skin.attr.SkinAttr;
import com.baimeng.framelibrary.skin.attr.SkinView;
import com.baimeng.framelibrary.skin.callback.ISkinChangeListener;
import com.baimeng.framelibrary.skin.config.SkinPreUtils;
import com.baimeng.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.baimeng.library.base.BaseActivity;
import com.baimeng.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/2/12.
 * Version 1.0
 * Description:
 */
public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory , ISkinChangeListener {
    // 后面会写插件换肤 预留的东西
    private SkinAppCompatViewInflater mAppCompatViewInflater;

    private String TAG = "BaseSkinActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //仿源码拦截view创建
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        //设置Factory
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
    }

    //每个控件创建时都会回调这个方法
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        // 拦截到View的创建  获取View之后要去解析
        // 1. 创建View
        // If the Factory didn't handle it, let our createView() method try
        View view = createView(parent, name, context, attrs);

        // 2. 解析属性  src  textColor  background  自定义属性
        // Log.e(TAG, view + "");

        // 2.1 一个activity的布局肯定对应多个这样的 SkinView

        //如果创建好的view不为null，就去解析view的属性
        if(view != null) {
            //将解析好的可以换肤的属性存入集合
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            //将view和属性集合封装成SkinView
            SkinView skinView = new SkinView(view,skinAttrs);
            // 3.统一交给SkinManager管理
            managerSkinView(skinView);
            //检查是否需要换肤
            SkinManager.getInstance().checkChangeSkin(skinView);
        }
        return view;
    }

    /**
     * 统一管理SkinView
     */
    private void managerSkinView(SkinView skinView){
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if(skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(skinView);
    }


    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unRegister(this);
        super.onDestroy();
    }
}
