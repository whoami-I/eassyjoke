package com.baimeng.framelibrary.recyclerview.commonadapter.wrapper;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/8/13.
 * 包裹原始Adapter的Adapter，添加头部和尾部
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //原始adapter,不包含头部和尾部
    private RecyclerView.Adapter mAdapter ;

    //头部和底部的集合要用map进行标识
    private SparseArray<View> mHeader , mFooter ;

    private static int BASE_HEADER_KEY = 1000000 ;
    private static int BASE_FOOTER_KEY = 2000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        mHeader = new SparseArray<>() ;
        mFooter = new SparseArray<>() ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeader.indexOfKey(viewType) >= 0){
            return createHeaderFooterViewHolder(mHeader.get(viewType));
        }else if(mFooter.indexOfKey(viewType) >= 0){
            return createHeaderFooterViewHolder(mFooter.get(viewType));
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    /**
     * 创建头部和尾部的viewholder
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int numHeader = mHeader.size() ;
        if(position < numHeader){
            return;
        }

//        if(position > mHeader.size() + mAdapter.getItemCount() ){
//            return;
//        }
        //头部和尾部都不用绑定数据
        int adjPostion = position - mHeader.size() ;
        int adpterCount = 0 ;
        if(mAdapter != null){
            adpterCount = mAdapter.getItemCount() ;
            if(adjPostion < adpterCount){
                mAdapter.onBindViewHolder(holder,adjPostion); ;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeader.size() + mFooter.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < mHeader.size()){
            return mHeader.keyAt(position) ;
        }

        int adjPostion = position - mHeader.size() ;
        int adpterCount = 0 ;
        if(mAdapter != null){
            adpterCount = mAdapter.getItemCount() ;
            if(adjPostion < adpterCount){
                return mAdapter.getItemViewType(adjPostion) ;
            }
        }

        return mFooter.keyAt(adjPostion - adpterCount);
    }

    //添加头部视图
    public void addHeaderView(View headerView){
        if(mHeader.indexOfValue(headerView) == -1){
           mHeader.put(BASE_HEADER_KEY++ , headerView);
        }
    }

    //添加尾部视图
    public void addFooterView(View footerView){
        if(mFooter.indexOfValue(footerView) == -1){
            mFooter.put(BASE_FOOTER_KEY++ , footerView);
        }
    }

    //移除头部视图
    public void removeHeaderView(View headerView){
        if(mHeader.indexOfValue(headerView) >= 0){
            mHeader.removeAt(mHeader.indexOfValue(headerView));
        }
    }

    //移除尾部视图
    public void removeFooterView(View footerView){
        if(mHeader.indexOfValue(footerView) >= 0){
            mHeader.removeAt(mHeader.indexOfValue(footerView));
        }
    }
}
