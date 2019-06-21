package com.baimeng.framelibrary.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/8/9.
 */

public class DotIndicatorView extends View {
    private Drawable mDrawable ;
    public DotIndicatorView(Context context) {
        this(context,null);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable ;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if(mDrawable != null){
//            mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
//            mDrawable.draw(canvas);
//        }
        Bitmap bitmap = drawableToBitmap(mDrawable);

        Bitmap circleBitmap = getCircleBitmap(bitmap);

        canvas.drawBitmap(circleBitmap,0,0,null);
    }

    /**
     * 获取到圆形的bitmap
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredHeight()/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,paint);
        bitmap.recycle();
        bitmap = null ;
        return circleBitmap ;
    }

    /**
     * 将drawable转换为bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable){
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap ;
        }
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
