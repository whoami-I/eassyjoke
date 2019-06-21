package com.baimeng.eassyjoke.imageselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.recyclerview.commonadapter.RecyclerViewCommonAdapter;
import com.baimeng.framelibrary.recyclerview.commonadapter.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SelectImageListAdapter extends RecyclerViewCommonAdapter<ImageEntity> {
    private Context mContext ;
    private List<ImageEntity> mData ;
    //已选中的图片集合
    private List<ImageEntity> mResultList;
    private SelectImageListener mImageSelectListener;
    private int mMaxCount ;

    public SelectImageListAdapter(List<ImageEntity> mData, Context context, ArrayList<ImageEntity> mResultList,int mMaxCount) {
        super(R.layout.media_chooser_item, mData, context);
        this.mContext = context ;
        this.mData = mData ;
        this.mResultList = mResultList ;
        this.mMaxCount = mMaxCount ;
    }

    @Override
    protected void convert(ViewHolder holder, final ImageEntity imageEntity, int position) {
        if(TextUtils.isEmpty(imageEntity.path)){
            //显示相机，否则显示图片
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator,View.INVISIBLE);
            holder.setViewVisibility(R.id.image,View.INVISIBLE);
        }else {
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator,View.VISIBLE);
            holder.setViewVisibility(R.id.image,View.VISIBLE);
            final ImageView selectIndicator = holder.getView(R.id.media_selected_indicator);
            ImageView imageView = holder.getView(R.id.image);
            Glide.with(mContext).load(imageEntity.path)
                    .centerCrop().into(imageView);
            if(mResultList != null){
                if(mResultList.contains(imageEntity)){
                    selectIndicator.setSelected(true);
                }else {
                    selectIndicator.setSelected(false);
                }
            }
            holder.setOnItemClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mResultList.contains(imageEntity)){
                        mResultList.remove(imageEntity);
                    }else {
                        if(mResultList.size() >= mMaxCount){
                            Toast.makeText(mContext,"最多只能添加"+mMaxCount+"张图片哦",Toast.LENGTH_SHORT).show();
                        }
                        mResultList.add(imageEntity);
                    }
                    notifyDataSetChanged();
                    if (mImageSelectListener != null){
                        mImageSelectListener.onSelectImage();
                    }
                }
            });

        }
    }

    public void setOnSelectImageListener(SelectImageListener listener){
        this.mImageSelectListener = listener ;
    }

}
