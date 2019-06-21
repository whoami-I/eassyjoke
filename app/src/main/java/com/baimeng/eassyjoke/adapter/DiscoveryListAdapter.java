package com.baimeng.eassyjoke.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.baimeng.eassyjoke.R;
import com.baimeng.eassyjoke.mode.DiscoverListResult;
import com.baimeng.framelibrary.recyclerview.commonadapter.RecyclerViewCommonAdapter;
import com.baimeng.framelibrary.recyclerview.commonadapter.ViewHolder;

import java.util.List;

import com.baimeng.eassyjoke.ImageLoader.GlideImageLoader;

/**
 * Created by BaiMeng on 2017/8/14.
 */
public class DiscoveryListAdapter extends
        RecyclerViewCommonAdapter<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> {
    public DiscoveryListAdapter(List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> mData,
                                Context context) {
        super(R.layout.channel_list_item, mData, context);
    }

    @Override
    protected void convert(ViewHolder holder,
                           DiscoverListResult.DataBean.CategoriesBean.CategoryListBean item,
                           int position) {
        // 显示数据
        String str = item.getSubscribe_count() + " 订阅 | " +
                "总帖数 <font color='#FF678D'>" + item.getTotal_updates() + "</font>";
        holder.setText(R.id.channel_text, item.getName())
                .setText(R.id.channel_topic, item.getIntro())
                .setText(R.id.channel_update_info, Html.fromHtml(str));

        // 是否是最新
        if (item.isIs_recommend()) {
            holder.setViewVisibility(R.id.recommend_label, View.VISIBLE);
        } else {
            holder.setViewVisibility(R.id.recommend_label, View.GONE);
        }
        // 加载图片
        holder.setImagePath(R.id.channel_icon, new GlideImageLoader(item.getIcon_url()));
    }
}
