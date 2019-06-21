package com.baimeng.framelibrary.base;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.baimeng.framelibrary.R;
import com.baimeng.library.navigation.AbsNavigationBar;
import com.baimeng.library.utils.LogUtils;

/**
 * Created by Administrator on 2017/7/13.
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationBarParams> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationBarParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 绑定效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightText);

        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        // 左边 要写一个默认的  finishActivity
        setOnClickListener(R.id.back,getParams().mLeftClickListener);

        setVisibility(R.id.back,getParams().leftIconVisible);
    }

    private void setVisibility(int viewId, int visibility) {
        findViewById(viewId).setVisibility(visibility);
    }

    public static class Builder extends AbsNavigationBar.Builder{
        DefaultNavigationBarParams P ;

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationBarParams(context , null);
        }

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationBarParams(context , parent);
        }

        @Override
        public DefaultNavigationBar build() {
            DefaultNavigationBar navigationBar = new DefaultNavigationBar(P);
            Log.e("P====",P.toString());
            return navigationBar;
        }

        //设置所有效果

        public Builder setTitle(String title){
            P.mTitle = title ;
            return this ;
        }

        public Builder setLeftText(String text){
            P.mLeftText = text ;
            return this ;
        }

        public Builder setRightText(String text){
            P.mRightText = text ;
            return this ;
        }

        public Builder setRightIcon( int res){
            P.mRightIcon = res ;
            return this ;
        }

        public Builder setLeftIcon( int res){
            P.mLeftIcon = res ;
            return this ;
        }


        public Builder setRightClick(View.OnClickListener listener){
            P.mRightClickListener = listener ;
            return this ;
        }

        public Builder setLeftClick( View.OnClickListener listener){
            P.mLeftClickListener = listener ;
            return this ;
        }

        public Builder hideLeftIcon() {
            P.leftIconVisible = View.INVISIBLE ;
            return this ;
        }


        public static class DefaultNavigationBarParams extends AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightIcon;
            public int mLeftIcon;
            public View.OnClickListener mLeftClickListener = new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ((Activity)mContext).finish();
                }
            };
            public View.OnClickListener mRightClickListener;
            public String mLeftText;
            public int leftIconVisible = View.VISIBLE;

            DefaultNavigationBarParams(Context context, ViewGroup parent) {
                super(context, parent);
            }

            @Override
            public String toString() {
                return "DefaultNavigationBarParams{" +
                        "mTitle='" + mTitle + '\'' +
                        ", mRightText='" + mRightText + '\'' +
                        ", mRightIcon=" + mRightIcon +
                        ", mLeftIcon=" + mLeftIcon +
                        ", mLeftClickListener=" + mLeftClickListener +
                        ", mRightClickListener=" + mRightClickListener +
                        ", mLeftText='" + mLeftText + '\'' +
                        ", leftIconVisible=" + leftIconVisible +
                        '}';
            }
        }
    }
}
