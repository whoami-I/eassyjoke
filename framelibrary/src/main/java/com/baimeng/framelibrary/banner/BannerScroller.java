package com.baimeng.framelibrary.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/8/9.
 */

public class BannerScroller extends Scroller {

    //页面切换动画时间
    private int mScrollerDuration = 1000 ;
    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }

    /**
     * 设置页面切换动画时间
     * @param duration
     */
    public void setScrollerDuration(int duration){
        this.mScrollerDuration = duration ;
    }
}
