package com.baimeng.framelibrary.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/8/12.
 */

public class ListItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivide ;

    public ListItemDecoration(Context context , int drawableResId) {
        mDivide = ContextCompat.getDrawable(context, drawableResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(position != 0){
            outRect.top = mDivide.getIntrinsicHeight() ;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        Rect rect = new Rect() ;
        rect.left = parent.getPaddingLeft() ;
        rect.right = parent.getWidth() - parent.getPaddingRight() ;
        for (int i = 1; i < childCount; i++) {
            rect.bottom = parent.getChildAt(i).getTop() ;
            rect.top = rect.bottom - mDivide.getIntrinsicHeight() ;
            mDivide.setBounds(rect);
            mDivide.draw(c);
        }
    }
}
