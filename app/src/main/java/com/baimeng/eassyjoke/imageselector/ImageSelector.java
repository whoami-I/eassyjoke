package com.baimeng.eassyjoke.imageselector;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/15.
 * 图片选择链式调用
 */

public class ImageSelector {

    private int mSelectMode = ImageSelectorActivity.MODE_MULTI ;

    private ArrayList<ImageEntity> mData ;

    private int mMaxCount = 9 ;

    private boolean mShowCarame = true ;
    public static ImageSelector create(){
        return new ImageSelector();
    }
    public ImageSelector count(int count){
        this.mMaxCount = count ;
        return this ;
    }
    public ImageSelector multi(){
        this.mSelectMode = ImageSelectorActivity.MODE_MULTI ;
        return this ;
    }

    public ImageSelector single(){
        this.mSelectMode = ImageSelectorActivity.MODE_SINGLE ;
        return this ;
    }

    public ImageSelector origin(ArrayList<ImageEntity> data){
        this.mData = data ;
        return this ;
    }

    public ImageSelector showCarame(boolean show){
        this.mShowCarame = show ;
        return this ;
    }

    public void start(Activity activity , int requestCode){
        Intent intent = new Intent(activity,ImageSelectorActivity.class);
        addParamsByIntent(intent);
        activity.startActivityForResult(intent,requestCode);
    }

    public void start(Fragment fragment , int requestCode){
        Intent intent = new Intent(fragment.getActivity(),ImageSelectorActivity.class) ;
        addParamsByIntent(intent);
        fragment.getActivity().startActivityForResult(intent,requestCode);
    }

    private void addParamsByIntent(Intent intent) {
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT,9);
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE,ImageSelectorActivity.MODE_MULTI);
        if(mData == null){
            mData = new ArrayList<>() ;
        }
        intent.putExtra(ImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,mData);
        intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA,true);
    }
}
