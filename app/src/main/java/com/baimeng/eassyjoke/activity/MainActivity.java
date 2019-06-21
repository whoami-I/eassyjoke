package com.baimeng.eassyjoke.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.base.BaseApplication;
import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.base.DefaultNavigationBar;
import com.baimeng.framelibrary.skin.SkinManager;
import com.baimeng.framelibrary.skin.SkinResources;
import com.baimeng.library.dialog.AlertDialog;
import com.baimeng.library.fixbug.FixDexManager;
import com.baimeng.library.ioc.CheckNet;
import com.baimeng.library.ioc.OnClick;
import com.baimeng.library.ioc.ViewById;
import com.baimeng.library.utils.LogUtils;
import com.baimeng.library.utils.XPermissionUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseSkinActivity {
    @ViewById(R.id.sample_text)
    private Button tv;
    @ViewById(R.id.parent)
    private LinearLayout mParent;

    int i = 0;

   // private calculate aidl;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //此处的aidl是一个代理
          //  aidl = calculate.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
           // aidl = null;
        }
    };

    @Override
    protected void initData() {
//        startService(new Intent(this, MessageService.class));
//        startService(new Intent(this, GuardService.class));
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            startService(new Intent(this, JobWakeUpService.class));
//        }
        //绑定服务
      //  bindService();
        tv.setText("口活的活好");

        //获取本地内存卡中的fix.patch
        XPermissionUtils.requestPermissions(this, 0, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //阿里的热修复
                // aliFixBug();

                //自己的热修复
                fixDexBug();
            }

            @Override
            public void onPermissionDenied() {

            }
        });

        //获取上次崩溃文件上传至服务器
//        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
//        if(crashFile.exists()){
//            //上传至服务器
//            try {
//                InputStreamReader fileReader = new InputStreamReader(new FileInputStream(crashFile));
//                char[] chars = new char[1024];
//                int len = 0 ;
//                while ((len = fileReader.read(chars))!= -1){
//                    String str = new String(chars,0,len);
//                    Log.e("TAG",str) ;
//                }
//            }catch (Exception e){
//
//            }
//        }

//        AsyncTask <Void,String,Void> task = new AsyncTask<Void,String ,Void>(){
//            @Override
//            protected Void doInBackground(Void... params) {
//                return null;
//            }
//        };
//        task.execute();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.sinieco.mplinechart");
        intent.setPackage("com.sinieco.mplinechart");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
        if (fixFile.exists()) {
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(this, "我的修复成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "我的修复失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void aliFixBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
            try {
                BaseApplication.patchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(MainActivity.this, "Bug修复成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Bug修复失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initView() {
        XPermissionUtils.requestPermissions(this, 0, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Log.i("TAG==========", "获取到权限");
                        //请求网络
                        requestNet();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.i("TAG==========", "拒绝权限");
                    }
                });
    }

    private void requestNet() {
        //测试联网框架
        Map<String, Object> params = new HashMap<>();
        params.put("key", "a0126d32f1215b0e769fd7c352bafd01");
//        HttpUtils.with(this).
//                isCache(true).
//                get().url("http://v.juhe.cn/WNXG/city")
//                .addParams(params)
//                .execute(new HttpCallBack<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.i("成功============",result);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.i("失败===========",e.toString());
//                    }
//                });

//        HttpUtils.with(this).isCache(true).get().url("http://v.juhe.cn/WNXG/city")
//                .addParams(params)
//                .execute(new HttpCallBack<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.i("成功",result);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.i("失败",e.toString());
//                    }
//                });


//                .execute(new HttpCallBack<AResponse<List<AMainIndicatorShowDto>>>() {
//                    @Override
//                    public void onError(Exception e) {
//                        Log.i("Error++++++++++++++",e.toString());
//                    }
//
//                    @Override
//                    public void onSuccess(AResponse<List<AMainIndicatorShowDto>> result) {
//                        Log.i("返回实体=========",result.toString());
//                    }
//                });
    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setLeftText("返回")
                .setTitle("投稿")
                .setRightText("发布")
                .setRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(MainActivity.class);
                    }
                })
                .setLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .build();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @OnClick({R.id.sample_text, R.id.iv_img})
    @CheckNet
    private void onClick(View view) {
        if (view.getId() == R.id.sample_text) {
//            try {
//                Toast.makeText(this, "计算结果是：" + aidl.add(3, 4), Toast.LENGTH_SHORT).show();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }

            LogUtils.e("text点击");
            showDialog();
            //恢复默认皮肤
            SkinManager.getInstance().restoreDefault();
        } else if (view.getId() == R.id.iv_img) {
//            Toast.makeText(this, "测试："+ (2/0), Toast.LENGTH_SHORT).show();
            Log.e("=======", "img点击");
            Log.e("什麽貴？", "新皮膚");
            String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "blue.skin";
            int result = SkinManager.getInstance().loadSkin(skinPath);
            Log.e("换肤情况","=================================="+result);
            // changeSkin();
            //  showDialog();

            //测试数据库框架
            //  IDaoSupport<Person> dao = DaoSupportFactory.getFactory().getDao(Person.class);
            //插入测试
//            List<Person> persons = new ArrayList<>();
//            for (int i = 0 ; i < 5000 ; i++){
//                persons.add(new Person("张思宁==="+i,23));
//            }
//            long start = System.currentTimeMillis();
//            dao.insert(persons);
//            Toast.makeText(this,"耗时===="+(System.currentTimeMillis() - start) ,Toast.LENGTH_SHORT).show();
//            //dao.insert();

            //查询测试

//            List<Person> query = dao.query();
//            Toast.makeText(this,"差到"+query.size()+"条数据",Toast.LENGTH_SHORT).show();

//            //删除数据
//            dao.delete("name",new String[]{"张思宁===2"});

//            //更改数据
//            Person person = new Person("张三", 11);
//            dao.update(person,"name","张思宁===0");

            //查询支持类的使用
//            List<Person> list = dao.querySupport().selection("name").selectionArgs("张三").columns("name","age").query();
//            Log.i("查询结果：",list.toString());

            //换肤

        }

    }

    private void changeSkin() {
        LogUtils.e("什麽鬼？");
        LogUtils.i("i===" + i + "====i%2======" + i % 2);
//        i++ ;
//        if(i%2 == 0){
//            //默认皮肤
//            LogUtils.i("默認皮膚");
//            int result = SkinManager.getInstance().restoreDefault();
//
//        }else if(i%2 == 1){
        LogUtils.i("新皮膚");
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "blue.skin";
        int result = SkinManager.getInstance().loadSkin(skinPath);
//        }

    }

    /**
     * 换肤基础
     */
//    private void changeSkin() {
//        try {
//            //获取当前app的Resources
//            Resources superRes = getResources() ;
//            //通过反射获取到AssetManager实例
//            AssetManager am = AssetManager.class.newInstance();
//            //反射获取到addAssetPath方法
//            Method addAssetMothod = AssetManager.class.getMethod("addAssetPath", String.class);
//            //反射调用addAssetPath方法
//            addAssetMothod.invoke(am,Environment.getExternalStorageDirectory().getAbsolutePath()+
//                    File.separator+"other.skin");
//            //获取皮肤apk的Resources
//            Resources resources = new Resources(am,superRes.getDisplayMetrics(),superRes.getConfiguration());
//            //获取图片资源的id
//            int drawableId = resources.getIdentifier("image_src", "drawable", "com.baimeng.myapplication");
//            //获取图片的Drawable
//            Drawable drawable = resources.getDrawable(drawableId);
//            //设置图片
//            mSkin.setImageDrawable(drawable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setContentView(R.layout.dialog)
                .setText(R.id.weibo, "微博")
                .setText(R.id.weixin, "微信")
                .setOnClickListener(R.id.share, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
                    }
                })
                .fromBottom(true)
                .fullWidth()
                .show();
        Context context = getApplicationContext();

    }

    @Override
    public void changeSkin(SkinResources skinResources) {
        // 改变皮肤监听
        //控制自定义控件护肤
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}