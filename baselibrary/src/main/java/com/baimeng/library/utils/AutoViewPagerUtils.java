package com.baimeng.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

/**
 * ViewPager自动无限轮播图的封装类
 * <p>
 * Created by BaiMeng on 2016/12/29.
 */
public class AutoViewPagerUtils {

    private AppCompatActivity ctx;
    private ArrayList<Fragment> urls;
    private int ErrImgID;
    private AutoAdapter adapter;
    private int currPosition;
    private int currIndex;
    private Handler handler;
    private final int HANDLER_TURN_NEXT = 100;
    private int delayMillis = 8 * 1000;
    private OnPagerClickListener clickListener;
    private OnPagerChangeListener changeListener;

    /**
     * 构造方法。需调用startPlay或startPlayDelayed方法开启轮播。
     *
     * @param ctx       上下文
     * @param vp        ViewPager对象
     * @param fragments
     * @param ErrImgID  当集合为空时，显示的图片ID
     */
    public AutoViewPagerUtils(AppCompatActivity ctx, final ViewPager vp, final ArrayList<Fragment> fragments, int ErrImgID) {
        this.ctx = ctx;
        this.urls = fragments;
        this.ErrImgID = ErrImgID;
        adapter = new AutoAdapter(ctx.getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(currPosition);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_TURN_NEXT:
                        if (currPosition == urls.size() - 1) {
                            currPosition = -1;
                        }
                        vp.setCurrentItem(++currPosition);
                        handler.sendEmptyMessageDelayed(HANDLER_TURN_NEXT, delayMillis);
                        break;
                    default:
                        break;
                }
            }
        };
        vp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        startPlayDelayed();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        if (urls != null && urls.size() != 0)
            vp.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageSelected(int p) {
                    if (p == urls.size()) {
                        currIndex = 0;
                        currPosition = 0;
                    } else {
                        currIndex = p;
                    }
                    if (changeListener != null)
                        changeListener.onPagerChange(currIndex);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });
    }

    /**
     * 设置轮播间隔时间(默认3秒)
     *
     * @param delayMillis
     */
    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    /**
     * 获取当前位置
     *
     * @return
     */
    public int getCurrIndex() {
        if (urls == null || urls.size() == 0) {
            return 0;
        } else {
            return currPosition % urls.size();
        }
    }

    /**
     * 设置单项点击事件监听
     *
     * @param clickListener
     */
    public void setOnPagerClickListener(OnPagerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 设置页面改变监听
     *
     * @param changeListener
     */
    public void setOnPagerChangeListener(OnPagerChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 延迟开始轮播(推荐onResume中调用)
     */
    public void startPlayDelayed() {
        if (urls != null && urls.size() != 0)
            if (!handler.hasMessages(HANDLER_TURN_NEXT))
                handler.sendEmptyMessageDelayed(HANDLER_TURN_NEXT, delayMillis);
    }

    /**
     * 立即开启轮播
     */
    public void startPlay() {
        if (urls != null && urls.size() != 0)
            if (!handler.hasMessages(HANDLER_TURN_NEXT))
                handler.sendEmptyMessage(HANDLER_TURN_NEXT);
    }

    /**
     * 结束轮播(推荐onStop中调用)
     */
    public void stopPlay() {
        if (urls != null && urls.size() != 0)
            handler.removeMessages(HANDLER_TURN_NEXT);
    }

    class AutoAdapter extends FragmentPagerAdapter {


        public AutoAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % urls.size();
            return urls.get(index);
        }

    }


    public interface OnPagerClickListener {
        void onPagerClick(View view, int position);
    }

    public interface OnPagerChangeListener {
        void onPagerChange(int position);
    }

}
