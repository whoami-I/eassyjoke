package com.baimeng.eassyjoke.ImageLoader;

import android.widget.ImageView;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.recyclerview.commonadapter.ViewHolder;
import com.bumptech.glide.Glide;


/**
 * Created by BaiMeng on 2017/8/14.
 */
public class GlideImageLoader extends ViewHolder.HolderImageLoader {
    public GlideImageLoader(String path) {
        super(path);
    }

    @Override
    public void loadImage(ImageView imageView, String path) {
        Glide.with(imageView.getContext()).load(path).placeholder(R.drawable.ic_discovery_default_channel)
                .centerCrop().into(imageView);
    }
}
