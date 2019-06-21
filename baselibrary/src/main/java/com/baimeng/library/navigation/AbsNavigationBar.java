package com.baimeng.library.navigation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baimeng.library.dialog.AlertDialog;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/7/13.
 * 头部的基类
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;

    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }

    public <T extends View> T findViewById(int viewId) {
        return (T) mNavigationView.findViewById(viewId);
    }

    //创建并绑定View
    private void createAndBindView() {
        if (mParams.mParent == null) {
//            ViewGroup activityRootView = (ViewGroup) ((Activity) mParams.mContext)
//                    .findViewById(android.R.id.content);
//            mParams.mParent = (ViewGroup) activityRootView.getChildAt(0);


            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        if (mParams.mParent == null) {
            return;
        }
        mNavigationView = LayoutInflater.from(mParams.mContext)
                .inflate(bindLayoutId(), mParams.mParent, false);
        //将头部添加到索引为0的位置
        mParams.mParent.addView(mNavigationView, 0);
        applyView();
    }


    public abstract static class Builder {
        //        AbsNavigationParams P ;
        public Builder(Context context, ViewGroup parent) {
            //创建参数对象
//            P = new AbsNavigationParams(context ,parent ) ;
        }

        public abstract AbsNavigationBar build();

        public static class AbsNavigationParams {
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
