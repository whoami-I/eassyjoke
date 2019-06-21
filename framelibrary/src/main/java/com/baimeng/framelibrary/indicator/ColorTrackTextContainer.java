package com.baimeng.framelibrary.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.baimeng.framelibrary.R;


/**
 * Created by BaiMeng on 2017/8/7.
 */
public class ColorTrackTextContainer extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private IndicatorGroupView mInnerContainer ;
    private int mTabVisibleNums = 0 ;
    private IndicatorAdapter mAdapter ;
    private ViewPager mViewPager ;
    private int mItemWidth;
    private int mCurrentPosition = 0 ;
    //解决点击抖动
    private boolean mIsExecuteScroll = false ;


    public ColorTrackTextContainer(Context context) {
        this(context,null);
    }

    public ColorTrackTextContainer(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextContainer);
        mTabVisibleNums = array.getInt(R.styleable.ColorTrackTextContainer_tabVisibleNums, mTabVisibleNums);
        array.recycle();
    }

    public ColorTrackTextContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInnerContainer = new IndicatorGroupView(getContext()) ;
        addView(mInnerContainer);
    }

    public void setAdapter(IndicatorAdapter adapter){
        if(adapter == null){
            throw new NullPointerException("adapter is Null") ;
        }
        mAdapter = adapter ;
        int itemCount = adapter.getCount();
        for(int i = 0 ; i < itemCount ; i++ ){
            View view = (View) adapter.getView(i, mInnerContainer);
            mInnerContainer.addItemView(view);
            if(mViewPager != null){
                switchItemClick(view,i);
            }

        }
        mAdapter.highLightIndicator(mInnerContainer.getItemView(0));

    }

    private void switchItemClick(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position,false);
                // 移动IndicatorView
                smoothScrollIndicator(position);
                //移动下标
                mInnerContainer.scrollBottomTrack(position);
            }
        });
    }

    /**
     * 点击移动，带动画
     * @param position
     */
    private void smoothScrollIndicator(int position) {
        float totalScroll = (position ) * mItemWidth ;
        int offsetScroll = (getWidth() - mItemWidth)/2 ;
        int finalScroll = (int) (totalScroll - offsetScroll) ;
        smoothScrollTo(finalScroll ,0);
    }

    public void setAdapter (IndicatorAdapter adapter , ViewPager viewPager){
        if(viewPager == null){
            throw new NullPointerException("ViewPager is null");
        }
        mViewPager = viewPager ;
        viewPager.addOnPageChangeListener(this);
        setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            mItemWidth = getItemWidth();
            int itemCount = mAdapter.getCount() ;
            for (int i = 0 ; i < itemCount ; i++){
               mInnerContainer.getItemView(i).getLayoutParams().width = mItemWidth ;
            }
            //添加底部跟踪的指示器
            mInnerContainer.addBottomTrackView(mAdapter.getBottomTrackView(),mItemWidth);
        }
    }

    private int getItemWidth() {
        int itemWidth = 0 ;
        int width = getWidth() ;
        int allWidth = 0 ;
        //如果设置了一屏显示几个
        if(mTabVisibleNums != 0){
            itemWidth = width / mTabVisibleNums ;
            return itemWidth ;
        }
        //如果没有设置一屏显示几个，我们要自己计算
        //获取到所有控件中的最宽的那个
        for (int i = 0 ; i < mAdapter.getCount() ; i++){
            if(itemWidth < mInnerContainer.getItemView(i).getWidth()){
                itemWidth = mInnerContainer.getItemView(i).getWidth() ;
            }
        }
        if(itemWidth * mAdapter.getCount() < width ){
            itemWidth = width / mAdapter.getCount() ;
            return itemWidth ;
        }
        for (int i = 0 ; i< mAdapter.getCount() ; i++){
            allWidth += itemWidth ;
            if(allWidth > width){
                itemWidth = width / i ;
                return itemWidth ;
            }
        }
        return itemWidth;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mIsExecuteScroll){
            //如果是点击就不要执行
            scrollCurrentIndicator(position,positionOffset);
            mInnerContainer.scrollBottomTrack(position , positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position) {
        //将当前位置点亮，重置上一个位置
        mAdapter.restoreIndicator(mInnerContainer.getItemView(mCurrentPosition));
        mCurrentPosition = position ;
        mAdapter.highLightIndicator(mInnerContainer.getItemView(mCurrentPosition));

    }

    @Override
    public void onPageScrollStateChanged(int state) {
       if(state == 1){
           mIsExecuteScroll = true ;
       }
       if(state == 0){
           mIsExecuteScroll = false ;
       }
    }

    /**
     * 滚动当前的指示器到中间
     * @param position
     * @param positionOffset
     */
    private void scrollCurrentIndicator(int position, float positionOffset) {
        float totalScroll = (position + positionOffset) * mItemWidth ;
        int offsetScroll = (getWidth() - mItemWidth)/2 ;
        int finalScroll = (int) (totalScroll - offsetScroll) ;
        scrollTo(finalScroll ,0);
    }
}
