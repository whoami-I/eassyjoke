package com.baimeng.eassyjoke.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.indicator.ColorTrackTextContainer;
import com.baimeng.framelibrary.indicator.IndicatorAdapter;
import com.baimeng.framelibrary.indicator.ColorTrackTextView;
import com.baimeng.library.base.BaseFragment;
import com.baimeng.library.ioc.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class HomeFragment extends BaseFragment {

    @ViewById(R.id.indicator_view)
    ColorTrackTextContainer mTopIndicator ;
    @ViewById(R.id.view_pager)
    ViewPager mViewPager ;

    private String [] items = new String [] {
            "直播","推荐","视频",
            "段友秀","图片","段子",
            "同城","精华","游戏"};
    private List<ColorTrackTextView> mTopItems ;

    @Override
    protected void initView() {
        mTopItems = new ArrayList<>() ;
        initIndicator();
        initViewPager();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ColorTrackTextView left = mTopItems.get(position);
                left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                left.setCurrentProgress(1-positionOffset);
                try {
                    ColorTrackTextView right = mTopItems.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        mTopIndicator.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public ColorTrackTextView getView(int position, ViewGroup parent) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ColorTrackTextView trackText = new ColorTrackTextView(getActivity());
                trackText.setOriginColor(Color.BLACK);
                trackText.setChangeColor(Color.RED);
                trackText.setTextSize(20);
                trackText.setText(items[position]);
                trackText.setGravity(Gravity.CENTER);
                trackText.setLayoutParams(params);
                trackText.setTextColor(Color.BLACK);
                mTopItems.add(trackText) ;
                return trackText;
            }

            @Override
            public void highLightIndicator(ColorTrackTextView view) {
                //这里的方向无所谓
                view.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                view.setCurrentProgress(1);
            }

            @Override
            public void restoreIndicator(ColorTrackTextView view) {
                //这里的方向无所谓
                view.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                view.setCurrentProgress(0);
            }

            @Override
            public View getBottomTrackView() {
                View view = new View (getActivity());
                view.setBackgroundColor(Color.RED);
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(88,8));
                return view;
            }
        },mViewPager);
    }
}
