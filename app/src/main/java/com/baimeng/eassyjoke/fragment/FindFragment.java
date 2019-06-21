package com.baimeng.eassyjoke.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.baimeng.eassyjoke.R;
import com.baimeng.eassyjoke.adapter.DiscoveryListAdapter;
import com.baimeng.eassyjoke.mode.DiscoverListResult;
import com.baimeng.framelibrary.banner.BannerAdapter;
import com.baimeng.framelibrary.banner.BannerView;
import com.baimeng.framelibrary.base.HttpCallBack;
import com.baimeng.framelibrary.recyclerview.commonadapter.wrapper.WrapperRecyclerView;
import com.baimeng.library.base.BaseFragment;
import com.baimeng.library.http.HttpUtils;
import com.baimeng.library.ioc.ViewById;
import com.baimeng.library.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/8/5.
 */

public class FindFragment extends BaseFragment {

    @ViewById(R.id.wrv_find_fragment)
    WrapperRecyclerView mRecyclerViewFindList ;

    @Override
    protected void initData() {
        HttpUtils.with(mContext).url("http://is.snssdk.com/2/essay/discovery/v3/?")
                .addParams("iid", 6152551759L)
                .addParams("aid", 7)
                .isCache(true)
                .execute(new HttpCallBack<DiscoverListResult>() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onSuccess(DiscoverListResult result) {
                        //LogUtils.e(result.toString());
                        showListData(result.getData().getCategories().getCategory_list());
                        addBannerView(result.getData().getRotate_banner().getBanners());
                    }

                });
    }

    private void addBannerView(final List<DiscoverListResult.DataBean.RotateBannerBean.BannersBean> banners) {
        if(banners.size() <= 0) return;
        BannerView bannerView = (BannerView) LayoutInflater.from(mContext).inflate(R.layout.layout_banner_view, mRecyclerViewFindList, false);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int positon, View convertview) {
                if(convertview == null){
                    convertview = new ImageView(mContext) ;
                }
                ((ImageView)convertview).setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(mContext).load(banners.get(positon).getBanner_url().getUrl_list()
                        .get(0).getUrl()).into((ImageView) convertview);
                return convertview;
            }

            @Override
            public int getCount() {
                return banners.size();
            }

            @Override
            public String getItemDesc(int postion) {
                return banners.get(postion).getBanner_url().getTitle();
            }
        });
        // 开启滚动
        bannerView.startRoll();
        mRecyclerViewFindList.addHeaderView(bannerView);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    private void showListData(List<DiscoverListResult.DataBean.CategoriesBean.CategoryListBean> list) {
        DiscoveryListAdapter adapter = new DiscoveryListAdapter(list, mContext);
        mRecyclerViewFindList.setAdapter(adapter);
        mRecyclerViewFindList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }
}
