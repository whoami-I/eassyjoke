package com.baimeng.eassyjoke.imageselector;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.base.DefaultNavigationBar;
import com.baimeng.framelibrary.skin.SkinResources;
import com.baimeng.library.ioc.ViewById;
import com.baimeng.library.utils.StatusBarUtil;
import com.baimeng.library.utils.XPermissionUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/14.
 * 图片选择器
 */

public class ImageSelectorActivity extends BaseSkinActivity implements View.OnClickListener  {

    //图片选择多选模式
    public static final int MODE_MULTI = 0x0011 ;
    //图片选择单选模式
    public static final int MODE_SINGLE = 0x0012 ;
    public static String EXTRA_SELECT_COUNT = "图片选择数量";
    public static String EXTRA_SELECT_MODE = "图片选择模式";
    public static String EXTRA_DEFAULT_SELECTED_LIST = "已选择的图片的地址集合";
    public static  String  EXTRA_SHOW_CAMERA = "是否显示相机";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "选择完成的图片集合";
    //图片单选还是多选int类型 type
    private int mMode = MODE_MULTI ;

    //boolean 类型的是否选择拍照按钮
    private boolean mShowCamera = true ;

    //int 类型图片张数
    private int mMaxCount = 8 ;

    //ArrayList<String> 已经选择好的图片
    private ArrayList<ImageEntity> mResultList ;
    //加载全部数据
    private static final int LOADER_TYPE = 0x0021;

    @ViewById(R.id.image_list_rv)
    private RecyclerView mImage_list_rv ;

    //图片选择的数量显示的textview
    @ViewById(R.id.select_num)
    private TextView mSelectNumTv;
    //预览textview
    @ViewById(R.id.select_preview)
    private TextView mSelectPreview;
    @ViewById(R.id.select_finish)
    private TextView mSelectFinish ;

    @Override
    public void changeSkin(SkinResources skinResources) {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT,mMaxCount);
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE,mMode);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA,mShowCamera);
        mResultList = (ArrayList<ImageEntity>) intent.getSerializableExtra(EXTRA_DEFAULT_SELECTED_LIST);

        XPermissionUtils.requestPermissions(this, 0, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        initImageList();
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });
        mSelectFinish.setOnClickListener(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar defaultNavigationBar =
                new DefaultNavigationBar.
                Builder(this).
                setTitle("所有图片")
                .build();
        StatusBarUtil.statusBarTintColor(this, Color.parseColor("#ff261f1f"));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_image_selector);
    }

    private void initImageList(){

        getLoaderManager().initLoader(LOADER_TYPE,null,mLoaderCallBack);
    }
    private LoaderManager.LoaderCallbacks<? extends Object> mLoaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA ,
                MediaStore.Images.Media.DISPLAY_NAME ,
                MediaStore.Images.Media.DATE_ADDED ,
                MediaStore.Images.Media.MIME_TYPE ,
                MediaStore.Images.Media.SIZE ,
                MediaStore.Images.Media._ID ,
        };
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //类似查询数据库
            CursorLoader cursorLoader = new CursorLoader(ImageSelectorActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR "
                            + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //解析封装集合
            if(data != null && data.getCount() > 0){
                ArrayList<ImageEntity> images = new ArrayList<>() ;
                if(mShowCamera){
                    images.add(new ImageEntity());
                }
                //不断遍历循环
                while(data.moveToNext()){
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                    //判断文件是否存在
//                    if(!pathExist(path)){
//                        continue;
//                    }
                    //封装成ImageEntity对象
                    ImageEntity image = new ImageEntity(path,name,dateTime);
                    images.add(image);

                }
                showListData(images);
            }
        }



        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private boolean pathExist(String path) {
        return false;
    }

    /**
     * 显示图片列表
     * @param images
     */
    private void showListData(ArrayList<ImageEntity> images) {
        SelectImageListAdapter listAdapter = new SelectImageListAdapter(images,this,mResultList,mMaxCount);
        mImage_list_rv.setLayoutManager(new GridLayoutManager(this , 4));
        listAdapter.setOnSelectImageListener(new SelectImageListener(){
            @Override
            public void onSelectImage() {
                exchangeViewShow();
            }
        });
        mImage_list_rv.setAdapter(listAdapter);
    }

    private void exchangeViewShow (){
        if(mResultList.size() > 0){
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        }else {
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }
        mSelectNumTv.setText(mResultList.size()+"/"+mMaxCount);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.select_finish:
                sureSelect();
                break ;
            case R.id.select_preview :
                //预览textviewdian点击事件
                break ;
        }
    }

    public void sureSelect(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT,mResultList);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍完照完成一下步骤
        //1.把照片加入集合

        //2.调用sureSelect()方法

        //3.通知系统本地图片改变，下次进来可以找到这张图片
        //mTempFile图片文件
       // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile)));

    }
}
