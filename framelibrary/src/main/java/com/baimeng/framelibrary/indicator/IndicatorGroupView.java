package com.baimeng.framelibrary.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/8/7.
 * indicator容器，包含textview和底部线形指示器
 */

public class IndicatorGroupView extends FrameLayout {

    private LinearLayout mIndicatorGroup ;
    private View mBottomTrackView ;
    //一个条目的宽度
    private int mItemWidth ;
    //底部指示器的LayoutParams
    private LayoutParams mTrackParams;

    private int mInitLeftMargin ;

    public IndicatorGroupView(@NonNull Context context) {
        this(context,null);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorGroup = new LinearLayout(context) ;
        addView(mIndicatorGroup);
    }


    public void addItemView(View itemView) {
        mIndicatorGroup.addView(itemView);
    }

    /**
     * 获取当前position的itme
     * @param position
     * @return
     */
    public View getItemView(int position){
        return mIndicatorGroup.getChildAt(position);
    }

    //添加底部跟踪的指示器
    public void addBottomTrackView(View bottomTrackView , int itemWidth) {
        if(bottomTrackView == null) return;
        this.mItemWidth = itemWidth ;
        this.mBottomTrackView = bottomTrackView ;
        //添加底部跟踪的view
        addView(mBottomTrackView);
        //添加到底部
        mTrackParams = (LayoutParams)mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM ;
        int trackWidth = mTrackParams.width ;
        if(mTrackParams.width == ViewGroup.LayoutParams.MATCH_PARENT){
            trackWidth = mItemWidth ;
        }
        //如果设置的宽度过大
        if(trackWidth > mItemWidth){
            trackWidth = mItemWidth ;
        }
        mTrackParams.width = trackWidth ;
        //设置bottomTrackView在中间
        mInitLeftMargin = (mItemWidth -trackWidth) / 2 ;
        mTrackParams.leftMargin = mInitLeftMargin ;

    }

    //滚动底部指示器
    public void scrollBottomTrack(int position, float positionOffset) {
        if(mBottomTrackView == null) return;
        int leftMargin = (int) ((position+positionOffset)*mItemWidth) ;
        mTrackParams.leftMargin = leftMargin + mInitLeftMargin ;
        mBottomTrackView.setLayoutParams(mTrackParams);

    }

    /**
     * 滚动底部指示器 点击移动带动画
     * @param position
     */
    public void scrollBottomTrack(int position) {
        if(mBottomTrackView == null) return;
        int finalLeftMargin = (int) ((position)*mItemWidth) + mInitLeftMargin ;
        //当前位置
        int currentLeftMargin = mTrackParams.leftMargin;

        //移动的距离
        int distance = finalLeftMargin - currentLeftMargin ;

        //带动画

        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin).setDuration((long) (Math.abs(distance)*0.4f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //会不断的回调 不断的是指leftmargin
                float currentLeftMargin = (float)animation.getAnimatedValue();
                mTrackParams.leftMargin = (int) currentLeftMargin ;
                mBottomTrackView.setLayoutParams(mTrackParams);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
