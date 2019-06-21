package com.baimeng.eassyjoke.imageselector.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SquareFrameLayout extends FrameLayout {
    public SquareFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SquareFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() ;
        int height = width ;
        setMeasuredDimension(width,height);
    }
}
