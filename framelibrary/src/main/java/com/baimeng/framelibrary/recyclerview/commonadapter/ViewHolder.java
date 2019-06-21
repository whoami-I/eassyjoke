package com.baimeng.framelibrary.recyclerview.commonadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/12.
 * RecyclerView通用ViewHolder
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews ;
    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>() ;
    }

    public ViewHolder setText(int resId , CharSequence text){
        TextView view = getView(resId);
        view.setText(text);
        return this ;
    }

    /**
     * 设置本地图片
     * @param resId view的id
     * @param imgId 图片的id
     * @return
     */
    public ViewHolder setImageResource(int resId , int imgId){
        ImageView img = getView(resId);
        img.setImageResource(imgId);
        return this ;
    }

    public ViewHolder setImagePath(int resId , HolderImageLoader imageLoader){
        ImageView view = getView(resId);
        imageLoader.loadImage(view,imageLoader.getPath());
        return this ;
    }

    public void setViewVisibility(int resId, int visible) {
        View view = getView(resId);
        view.setVisibility(visible);
    }

    public static abstract class HolderImageLoader{
        private String mPath ;
        public HolderImageLoader(String path){
            this.mPath = path ;
        }
        public abstract void loadImage(ImageView imageView , String path) ;

        public String getPath(){
            return mPath ;
        }
    }
    public ViewHolder setOnItemClickListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnItemLongClickListener(View.OnLongClickListener listener){
        itemView.setOnLongClickListener(listener);
        return this ;
    }

    public < T extends View> T getView(int resId) {
        View view = mViews.get(resId);
        if(view == null){
            view = itemView.findViewById(resId);
            if(view == null){
                throw new NullPointerException("没有找到id为"+resId+"的view");
            }else {
                mViews.put(resId,view);
                return (T)view ;
            }
        }else {
            return (T)view ;
        }
    }
}
