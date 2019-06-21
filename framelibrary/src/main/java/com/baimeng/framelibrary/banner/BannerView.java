package com.baimeng.framelibrary.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baimeng.framelibrary.R;


/**
 * Created by Administrator on 2017/8/9.
 */

public class BannerView extends RelativeLayout {

    //轮播图ViewPager
    private BannerViewPager mBannerVP;
    //图片描述
    private TextView mBannerDesc;
    //点的容器
    private LinearLayout mDotContainer;
    //自定义的BannerAdapter
    private BannerAdapter mAdapter ;

    private Context mContext ;
    //选中点的drawable
    private Drawable mIndicatorFocuDrawable ;
    //未选中点的drawable
    private Drawable mIndicatorNormalDrawable ;
    //当前指示点
    private int mCurrIndicator = 0 ;

    //自定义属性
    //指示点的位置，默认在左边
    private int mDotGravity = -1 ;
    //点的大小，默认8dp
    private int mDotSize = 8 ;
    //点的间距，默认8dp
    private int mDotDistance ;
    //指示点背景颜色
    private int mBottomColor = Color.TRANSPARENT ;
    //宽高比
    private float mWidthProportion,mHeightProportion;

    private View mBannerBV;

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context ;
        View view = inflate(context, R.layout.ui_banner_layout, this);
        initAttribute(context ,attrs);
        initView();
        mIndicatorFocuDrawable = new ColorDrawable(Color.RED);
        mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
    }

    /**
     * 初始化属性
     * @param attrs
     */
    private void initAttribute(Context context , AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity,mDotGravity);
        mIndicatorFocuDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if(mIndicatorFocuDrawable == null){
            //如果在布局文件中没有配置颜色或者drawable
            mIndicatorFocuDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if(mIndicatorNormalDrawable == null){
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE) ;
        }
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize,dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance,dip2px(mDotDistance));
        mBottomColor = array.getColor(R.styleable.BannerView_bottomColor,mBottomColor);
        mHeightProportion = array.getFloat(R.styleable.BannerView_heightProportion,mHeightProportion);
        mWidthProportion = array.getFloat(R.styleable.BannerView_widthProportion,mWidthProportion);
        array.recycle();
    }

    private void initView() {
        mBannerVP = (BannerViewPager)findViewById(R.id.banner_vp);
        mBannerDesc = (TextView) findViewById(R.id.banner_desc);
        mDotContainer = (LinearLayout)findViewById(R.id.dot_container);
        mBannerBV = findViewById(R.id.banner_bottom_view);
        mBannerBV.setBackgroundColor(mBottomColor);

    }

    /**
     * 设置适配器
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter){
        mAdapter = adapter ;
        mBannerVP.setAdapter(adapter) ;
        initDotIndicator();
        mBannerVP.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                //前一个选中点恢复
                DotIndicatorView last = (DotIndicatorView)mDotContainer.getChildAt(mCurrIndicator);
                last.setDrawable(mIndicatorNormalDrawable);
                //当前指示点选中
                mCurrIndicator = position % mAdapter.getCount() ;
                DotIndicatorView curr = (DotIndicatorView)mDotContainer.getChildAt(mCurrIndicator);
                curr.setDrawable(mIndicatorFocuDrawable);

                String desc = mAdapter.getItemDesc(mCurrIndicator);
                mBannerDesc.setText(desc);
            }
        });

        String desc = mAdapter.getItemDesc(mCurrIndicator);
        mBannerDesc.setText(desc);

        post(new Runnable() {
            @Override
            public void run() {
                //动态指定高度
                if(mHeightProportion == 0 || mWidthProportion == 0){
                    return;
                }
                int width = getMeasuredWidth();
                int height = (int) (width * mHeightProportion / mWidthProportion);
                getLayoutParams().height = height ;
               // mBannerBV.getLayoutParams().height = height;
            }
        });
    }

    private void initDotIndicator() {
        int count = mAdapter.getCount();
        mDotContainer.setGravity(getDotGravity());
        for (int i = 0 ; i < count ; i++){
            DotIndicatorView dot = new DotIndicatorView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize,mDotSize);
            dot.setLayoutParams(params);
            params.leftMargin = mDotDistance ;
            if(i == 0){
                dot.setDrawable(mIndicatorFocuDrawable);
            }else {
                dot.setDrawable(mIndicatorNormalDrawable);
            }
            mDotContainer.addView(dot);
        }
    }

        private int getDotGravity() {
        switch (mDotGravity){
            case 0 :
                return Gravity.CENTER ;
            case 1 :
                return Gravity.LEFT ;
            case -1 :
                return Gravity.RIGHT ;
        }
        return Gravity.RIGHT;
    }

    /**
     * dip转成像素
     * @param dip
     * @return
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,
                getResources().getDisplayMetrics());
    }

    /**
     * 开始滚动
     */
    public void startRoll() {
        mBannerVP.startRoll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mBannerVP.stopRoll();
                requestDisallowInterceptTouchEvent(true);
                return true ;
            case MotionEvent.ACTION_UP:
                mBannerVP.startRoll();
                requestDisallowInterceptTouchEvent(false);
                return false ;
        }
        return false ;
    }
}
