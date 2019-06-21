package com.baimeng.framelibrary.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class BannerViewPager extends ViewPager {

    private BannerAdapter mAdapter ;

    private final int SCROLL_MSG = 0x0011 ;
    private int mCutDownTime = 3500 ;
    private BannerScroller mScroller;
    private Activity mActivity ;

    private List<View> mConvertViews ;

    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context ;
        //设置切换速率
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            //第一个参数，要设置的对象，第二个参数表示要设置的值
            mScroller = new BannerScroller(context);
            field.set(this,mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //每隔几秒切换图片
            setCurrentItem(getCurrentItem()+1);

            startRoll();
        }
    };

    public void setAdapter (BannerAdapter adapter){
        this.mAdapter = adapter ;
        setAdapter(new BannerViewAdapter());
        mActivity.getApplication().registerActivityLifecycleCallbacks(activityLife);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public class BannerViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mAdapter.getView(position % mAdapter.getCount(),getConverView());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
            //复用convertview
            mConvertViews.add((View) object) ;
        }
    }

    //返回复用的界面
    private View getConverView() {
        for (int i = 0 ; i<mConvertViews.size() ; i++){
            if(mConvertViews.get(i).getParent() == null){
                return mConvertViews.get(i);
            }
        }
        return null ;
    }

    /**
     * 自动轮播
     */
    public void startRoll(){
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mCutDownTime);
    }

    public void stopRoll(){
        mHandler.removeMessages(SCROLL_MSG);
    }

    /**
     * activity销毁时回调，清除消息
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null ;
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(activityLife);
    }

    /**
     * 设置页面切换动画时间
     * @param duration
     */
    public void setScrollerDuration(int duration){
        mScroller.setScrollerDuration(duration);
    }


    Application.ActivityLifecycleCallbacks  activityLife = new DefaultActivityLifeCycleCallBack(){
        @Override
        public void onActivityResumed(Activity activity) {
            if(activity == mActivity){
                mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mCutDownTime);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if(activity == mActivity){
                mHandler.removeMessages(SCROLL_MSG);
            }
        }
    };
}
