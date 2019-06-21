package com.baimeng.framelibrary.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

/**
 * 全局异常捕捉类
 * Created by Administrator on 2017/7/6.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static ExceptionCrashHandler mInstance ;

    private Context mContext ;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance(){
        if(mInstance == null){
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance ;
    }


    public void init (Context context){
        this.mContext = context ;
        //设置全局的异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);

        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("TAG","报异常了");

        //写入异常信息和手机以及系统信息到本地文件

        //1.崩溃的详细信息

        //2.应用信息 ， 包名， 版本号
        
        String crashFileName = saveInfoToSD(e);

        
        cacheCrashFile(crashFileName);
        //上传到服务器

        //系统默认的异常类处理异常
        mDefaultExceptionHandler.uncaughtException(t,e);
    }

    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash",Context.MODE_APPEND);
        sp.edit().putString("CRASH_FILE_NAME",fileName).commit();
    }

    private String saveInfoToSD(Throwable e) {
        String fileName = null ;
        StringBuffer sb = new StringBuffer() ;
        //手机信息+应用信息
        for (Map.Entry<String , String > entry : obtainSimpleInfo(mContext).entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n") ;
        }
        //崩溃详细信息
        sb.append(obtainExceptionInfo(e));

        //手机应用目录，不需要动态申请权限
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(mContext.getFilesDir()+File.separator+"crash"+File.separator);
            //先删除之前的异常信息
            if(dir.exists()){
                deleteDir(dir);
            }

            //再重新创建文件夹
            if(!dir.exists()){
                dir.mkdir();
            }

            try {
                fileName =  dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm")+".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
        return fileName ;
    }

    private String getAssignTime(String dateFormatStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        return dateFormat.format(System.currentTimeMillis());
    }

    private boolean deleteDir(File dir) {
        if(dir.isDirectory()){
            File[] children = dir.listFiles();
            for (File child : children){
                child.delete();
            }
        }
        return true ;
    }

    private String obtainExceptionInfo(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString() ;
    }

    private HashMap<String,String> obtainSimpleInfo(Context mContext) {
        HashMap<String , String > map = new HashMap<>() ;
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null ;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName",packageInfo.versionName);
        map.put("versionCode",""+packageInfo.versionCode);
        map.put("MODEL",""+ Build.MODEL);
        map.put("SDK_INT", ""+Build.VERSION.SDK_INT);
        map.put("PRODUCT",""+Build.PRODUCT);
        map.put("MOBLE_INFO",getMobileInfo());
        return  map ;
    }

    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer() ;
        try {
            //利用反射获取Build的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                String name = field.getName() ;
                //应为都是静态属性所以可以传null
                String value = field.get(null).toString() ;
                sb.append(name+"="+value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString() ;
    }

    public File getCrashFile(){
        String crashFileName = mContext.getSharedPreferences("crash",Context.MODE_PRIVATE).getString("CRASH_FILE_NAME","");
        return new File(crashFileName);
    }
}
