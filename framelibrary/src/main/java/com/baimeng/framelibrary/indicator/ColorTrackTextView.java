package com.baimeng.framelibrary.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.baimeng.framelibrary.R;


/**
 * Created by Administrator on 2017/8/6.
 * 颜色跟踪TextView
 */

public class ColorTrackTextView extends TextView {
    //绘制不变色字体的画笔
    private Paint mOriginPaint ;
    //绘制变色字体的画笔
    private Paint mChangePaint ;
    //当前进度
    private float mCurrentProgress = 0f ;

    //当前朝向，默认是从左到右
    private Direction mDirection = Direction.LEFT_TO_RIGHT ;

    /**
     * 实现两种朝向的枚举类型
     */
    public enum Direction{
        RIGHT_TO_LEFT , LEFT_TO_RIGHT
    }


    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context,attrs);
    }

    private void initPaint(Context context , AttributeSet attr) {

        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.ColorTrackTextView);
        int orginColor = array.getColor(R.styleable.ColorTrackTextView_orginColor, getTextColors().getDefaultColor());
        int changeColor = array.getColor(R.styleable.ColorTrackTextView_changeColor, getTextColors().getDefaultColor());

        mOriginPaint = getPaintByColor(orginColor);
        mChangePaint = getPaintByColor(changeColor);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //系统的不能在使用了
        //super.onDraw(canvas);

        //计算分割线位置
        int width = getWidth() ;
        int middle = (int) (mCurrentProgress * width );

        //计算根据进度绘制不同颜色的文字，截取绘制文字的范围
        
//        drawChangeText(middle , canvas);
//        drawOrginText(middle ,canvas);

        String text = getText().toString() ;
        if (TextUtils.isEmpty(text)) return;
        //绘制不变色部分
        drawOriginText(text,middle ,canvas);
        //绘制变色部分
        drawChangeText(text , middle ,canvas);


    }

    private void drawChangeText(String text, int middle, Canvas canvas) {
        //判断当前的朝向
        if(mDirection == Direction.LEFT_TO_RIGHT){
            drawText(text,canvas,mChangePaint,0,middle);
        }else {
            drawText(text,canvas,mChangePaint,getWidth()-middle,getWidth());
        }

    }

    private void drawOriginText(String text, int middle , Canvas canvas) {
        //判断当前的朝向
        if(mDirection == Direction.LEFT_TO_RIGHT){
            drawText(text,canvas,mOriginPaint,middle,getWidth());
        }else {
            drawText(text,canvas,mOriginPaint,0,getWidth()-middle);
        }
    }

    private Paint getPaintByColor (int color){
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置颜色
        paint.setColor(color);
        //设置防抖动
        paint.setDither(true);
        paint.setTextSize(getTextSize());
        return paint ;
    }

    private void drawText(String text , Canvas canvas , Paint paint , int start , int end ){
        //保存画布状态
        canvas.save();
        //只绘制截取的部分
        canvas.clipRect(new RectF(start,0,end,getHeight()));
        //drawText 参数x 是字体的最左边的位置，等于控件宽度的一半减去字体宽度的一半(不考虑padding不相等的情况)
        Rect bounds = new Rect() ;
        paint.getTextBounds(text,0,text.length(),bounds);
        int x = getWidth() / 2 - bounds.width() / 2 ;
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int dy = bounds.height() / 2  - fontMetricsInt.bottom ;
        int baseLine = (getHeight() + bounds.height())/2 - dy ;
        //y代表BaseLine
        canvas.drawText(text,x,baseLine , paint);
        //释放画布
        canvas.restore();
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setCurrentProgress(float progress){
        this.mCurrentProgress = progress ;
        invalidate();
    }

    /**
     * 设置朝向
     * @param direction
     */
    public void setDirection(Direction direction){
        this.mDirection = direction ;
    }

    //设置原始不变色的字体颜色
    public void setOriginColor(int color){
      mOriginPaint.setColor(color);
    }

    //设置原始不变色的字体颜色
    public void setChangeColor(int color){
        mChangePaint.setColor(color);
    }

}
