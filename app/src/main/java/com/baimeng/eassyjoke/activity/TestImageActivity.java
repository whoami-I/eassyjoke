package com.baimeng.eassyjoke.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.baimeng.eassyjoke.R;
import com.baimeng.eassyjoke.imageselector.ImageEntity;
import com.baimeng.eassyjoke.imageselector.ImageSelector;
import com.baimeng.eassyjoke.imageselector.ImageSelectorActivity;
import com.baimeng.eassyjoke.imageselector.view.SquareImageView;
import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.base.DefaultNavigationBar;
import com.baimeng.framelibrary.recyclerview.commonadapter.RecyclerViewCommonAdapter;
import com.baimeng.framelibrary.recyclerview.commonadapter.ViewHolder;
import com.baimeng.framelibrary.skin.SkinResources;
import com.baimeng.library.ioc.ViewById;
import com.baimeng.library.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/14.
 */

public class TestImageActivity extends BaseSkinActivity {
    private final int SELECT_IMAGE_REQUEST = 0x0011;
    private ArrayList<ImageEntity> mImageList ;
    @ViewById(R.id.image_list_rv)
    private RecyclerView mRvImageList;
    private RecyclerViewCommonAdapter<ImageEntity> mAdapter;

    @Override
    public void changeSkin(SkinResources skinResources) {

    }

    @Override
    protected void initData() {
        mImageList = new ArrayList<>();
        mRvImageList.setLayoutManager(new GridLayoutManager(this,4));
        mAdapter = new RecyclerViewCommonAdapter<ImageEntity>(R.layout.item_image_list,
                mImageList, this) {
            @Override
            protected void convert(ViewHolder holder, ImageEntity imageEntity, int position) {
                SquareImageView img = holder.getView(R.id.sqv_img);
                Glide.with(TestImageActivity.this).load(imageEntity.path).into(img);
            }
        };
        mRvImageList.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setLeftText("返回")
                .setTitle("投稿")
                .setRightText("发布")
                .setLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build();
    }

    @Override
    protected void setContentView(){
        setContentView(R.layout.activity_test_image);
    }

    public void selectorImg(View view){
        ImageSelector.create().count(9).multi().showCarame(true).origin(mImageList)
                .start(this,SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE_REQUEST){
            if(resultCode == RESULT_OK && data != null){
                mImageList = (ArrayList<ImageEntity>) data.getSerializableExtra(ImageSelectorActivity.EXTRA_RESULT);
                mRvImageList.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
