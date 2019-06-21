package com.baimeng.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;

/**
 * Created by zhuangjj on 2017/1/18.
 */

public class ClipPicture {

    private Paint mPaint;
    private Rect mRect;
    private Bitmap target;

    private ClipPicture() {
    }

    private static ClipPicture instance;

    public synchronized static ClipPicture getInstance() {
        if (instance == null) {
            instance = new ClipPicture();
        }

        return instance;
    }

    /**
     * 设置图片的区域
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setRect(int left, int top, int right, int bottom) {
        if (mRect == null) {
            mRect = new Rect(left, top, right, bottom);
        }

        mRect.left = left;
        mRect.right = right;
        mRect.top = top;
        mRect.bottom = bottom;
    }

    private int bit_width = 0;
    private int bit_height = 0;

    public Bitmap clipPicture(Bitmap source) {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setAntiAlias(true);
//        mPaint.clearShadowLayer();
        getBitmapSize(source);
        if (mRect == null) {
            setRect(100, 200, 500, 600);
        }
        if (source != null) {
            target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
            Canvas canvas = new Canvas(target);
            canvas.drawRect(mRect, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, 0, 0, mPaint);
        }
//            source.recycle();
        return target;
    }

    /**
     * 截图图片
     *
     * @param src        原始的图片的bitmap
     * @param left       截取图片的左起始点
     * @param top        截取图片的右起始点
     * @param widthSize  截取图片的宽
     * @param heightSize 截取图片的高
     * @return
     */
    public Bitmap clipBitmap(Bitmap src, int left, int top, int widthSize, int heightSize) {
        if (src == null) {
            return null;
        }

        if (src.getWidth() == 0 || src.getHeight() == 0) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(widthSize, heightSize, Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(newBitmap);
        //数据的矫正
        left = left < 0 ? 0 : left;
        top = top < 0 ? 0 : top;

        //在原图上截取的区域
        Rect srcRect = new Rect(left, top,
                left + widthSize > src.getWidth() ? src.getWidth() : (left + widthSize),
                top + heightSize > src.getHeight() ? src.getHeight() : (top + heightSize)
        );

        Rect desRect = new Rect(0, 0, widthSize, heightSize);

        cavas.drawBitmap(src, srcRect, desRect, null);
        //cavas.save(Canvas.ALL_SAVE_FLAG);
        cavas.save();
        cavas.restore();

        return newBitmap;

    }

    /**
     * 截按照指定的矩形区域来截取图片
     *
     * @param src
     * @param rect
     * @return
     */
    public Bitmap clipBitmp(Bitmap src, Rect rect) {
        return clipBitmap(src, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }

    /**
     * 测试用例
     *
     * @param src
     * @return
     */
    public Bitmap clipBitmp(Bitmap src) {
//        if (src == null) {
//            return null;
//        }
//
//        if (src.getWidth() == 0 || src.getHeight() == 0) {
//            return null;
//        }
//        Bitmap newb = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//
//        Canvas cv = new Canvas(newb);
//
//        int left = 100;
//        int top = 0;
//
//        final Rect srcRect = new Rect(left, top, left + 300, top + 300);//在原图上截取的区域
//
//        final Rect dstRect = new Rect(0, 0, 300, 300);//把截取的图缩放至该矩形
//
//
//        cv.drawBitmap(src, srcRect, dstRect, null);
//
//        cv.save(Canvas.ALL_SAVE_FLAG);
//
//        cv.restore();

//        return newb;
        return clipBitmap(src, src.getWidth() / 4, src.getHeight() / 4, src.getWidth() / 2, src.getHeight() / 2);

    }

    private void getBitmapSize(Bitmap source) {
        bit_width = source.getWidth();
        bit_height = source.getHeight();

        int picWidth = bit_width > bit_height ? bit_height / 2 : bit_width / 2;
//        setRect(bit_width -3 * (bit_width/4 )
//                ,bit_height - 3 * (bit_height/4)
//                ,bit_width - (bit_width/4)
//                ,bit_height - (int)(bit_height/4+0.5));

        setRect(bit_width - 3 * (bit_width / 4)
                , bit_height - 3 * (bit_height / 4)
                , bit_width - 3 * (bit_width / 4) + picWidth
                , bit_height - 3 * (bit_height / 4) + picWidth);
    }

    public Bitmap clipPicture(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Bitmap bitMap = BitmapFactory.decodeFile(path);

        return clipPicture(bitMap);
    }
}
