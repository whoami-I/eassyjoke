package com.baimeng.framelibrary.recyclerview.commonadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 * recyclerview通用Adapter
 */

public abstract class RecyclerViewCommonAdapter<DATA> extends RecyclerView.Adapter<ViewHolder> {

    private int mLayoutId ;
    private List<DATA> mData ;
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener ;
    private ItemLongClickListener mItemLongClickListener ;
    private MulitiTypeSupport mTypeSupport ;

    public RecyclerViewCommonAdapter(int mLayoutId , List<DATA> mData , Context context) {
        this.mLayoutId = mLayoutId;
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
    }

    public RecyclerViewCommonAdapter(List<DATA> mData , Context context , MulitiTypeSupport mulitiTypeSupport) {
        this(-1,mData,context);
        this.mTypeSupport = mulitiTypeSupport ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mTypeSupport != null){
            mLayoutId = viewType ;
        }
        View itemView = mInflater.inflate(mLayoutId,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        bindData( holder ,position);
        if(mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (mItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClickListener(position);
                }
            });
        }
    }

    private void bindData(ViewHolder holder, int position){
        convert(holder,mData.get(position),position);
    }

    protected abstract void convert(ViewHolder holder, DATA data, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItmeClickListener(ItemClickListener listener){
        this.mItemClickListener = listener ;
    }

    public void setOnItemLongClickListener(ItemLongClickListener longClickListener){
        this.mItemLongClickListener = longClickListener ;
    }

    @Override
    public int getItemViewType(int position) {
        if(mTypeSupport != null){
            return mTypeSupport.getLayoutId(mData.get(position));
        }
        return super.getItemViewType(position);
    }
}
