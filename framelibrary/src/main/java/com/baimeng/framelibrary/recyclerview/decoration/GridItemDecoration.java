package com.baimeng.framelibrary.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/8/12.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivide ;

    public GridItemDecoration(Context context , int drawableResId) {
        mDivide = ContextCompat.getDrawable(context, drawableResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //留出分隔线的位置（下边和右边）
        int bottom = mDivide.getIntrinsicHeight() ;
        int right = mDivide.getIntrinsicWidth() ;
        if(isLastCloumn(view , parent)){
            right = 0 ;
        }

        if(inLastRow(view , parent)){
            bottom = 0 ;
        }
        outRect.bottom = bottom ;
        outRect.right = right ;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        //绘制水平分割线
        drawHorizontal( canvas ,  parent);
        //绘制竖直分割线
        drawVerital(canvas,parent);
    }

    private void drawVerital(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView.getLayoutParams();
            int top = childView.getTop() - params.topMargin ;
            int bottom = childView.getBottom() + params.bottomMargin ;
            int left = childView.getRight() + params.rightMargin ;
            int right = left + mDivide.getIntrinsicWidth() ;
            mDivide.setBounds(left,top,right,bottom);
            mDivide.draw(canvas);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView.getLayoutParams();
            int left = childView.getLeft() + params.leftMargin;
            int right = childView.getRight() + mDivide.getIntrinsicWidth() ;
            int top = childView.getBottom() + params.bottomMargin ;
            int bottom = top + mDivide.getIntrinsicHeight() ;
            mDivide.setBounds(left,top,right,bottom);
            mDivide.draw(canvas);
        }
    }

    /**
     * 是最右边一列
     * @return
     * @param view
     * @param parent
     */
    private boolean isLastCloumn(View view, RecyclerView parent) {
        //获取当前位置
        int currentPostion = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        int spanCount =  getSpanCount (parent) ;
        //获取列数

        return currentPostion % spanCount == spanCount -1 ;
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanCount = gridLayoutManager.getSpanCount();
            return spanCount ;
        }
        return 1;
    }

    /**
     * 是最下边一列
     * @return
     * @param view
     * @param parent
     */
    private boolean inLastRow(View view, RecyclerView parent) {
        //当前的位置  > 行数 - 1 *  列数
        //列数
        int spanCount = getSpanCount(parent);
        //行数
        int rowNum = 0 ;
        if( parent.getAdapter().getItemCount()%spanCount > 0){
            rowNum = parent.getAdapter().getItemCount() / spanCount + 1;
        }else {
            rowNum = parent.getAdapter().getItemCount() / spanCount ;
        }
         //当前位置
        int currentPostion = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewAdapterPosition() ;


        return currentPostion +1  > (rowNum -1) * spanCount ;
    }

}
