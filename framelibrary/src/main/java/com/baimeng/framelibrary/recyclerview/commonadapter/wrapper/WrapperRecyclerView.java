package com.baimeng.framelibrary.recyclerview.commonadapter.wrapper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/8/13.
 */

public class WrapperRecyclerView extends RecyclerView {

    private WrapRecyclerAdapter mAdapter;

    private AdapterDataObserver mAdapterObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            //super.onChanged();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            //super.onItemRangeChanged(positionStart, itemCount);
            mAdapter.notifyItemRangeChanged(positionStart,itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            //super.onItemRangeChanged(positionStart, itemCount, payload);
            mAdapter.notifyItemRangeChanged(positionStart, itemCount ,payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            //super.onItemRangeInserted(positionStart, itemCount);
            mAdapter.notifyItemRangeInserted(positionStart,itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            //super.onItemRangeRemoved(positionStart, itemCount);
            mAdapter.notifyItemRangeRemoved(positionStart,itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            //super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            mAdapter.notifyItemMoved(fromPosition,toPosition);
        }
    };

    public WrapperRecyclerView(Context context) {
        this(context,null);
    }

    public WrapperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WrapperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof WrapRecyclerAdapter){
            mAdapter = (WrapRecyclerAdapter) adapter;
        }else {
            mAdapter = new WrapRecyclerAdapter(adapter) ;
            adapter.registerAdapterDataObserver(mAdapterObserver);
        }
        super.setAdapter(mAdapter);
    }

    //添加头部视图
    public void addHeaderView(View headerView){
        if(mAdapter != null){
            mAdapter.addHeaderView(headerView);
        }
    }

    //添加尾部视图
    public void addFooterView(View footerView){
        if(mAdapter != null){
            mAdapter.addFooterView(footerView);
        }
    }

    //移除头部视图
    public void removeHeaderView(View headerView){
        if(mAdapter != null){
            mAdapter.removeHeaderView(headerView);
        }
    }

    //移除尾部视图
    public void removeFooterView(View footerView){
        if(mAdapter != null){
            mAdapter.removeFooterView(footerView);
        }
    }
}
