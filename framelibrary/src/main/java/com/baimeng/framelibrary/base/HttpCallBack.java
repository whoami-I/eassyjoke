package com.baimeng.framelibrary.base;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.baimeng.library.http.EngineCallBack;
import com.baimeng.library.http.HttpUtils;
import com.baimeng.library.utils.LogUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/16.
 */

public abstract class HttpCallBack<T> implements EngineCallBack {
    /**
     * 请求网络前封装参数
     *
     * @param context
     * @param params
     */
    public void onPreExecute(Context context, Map<String, Object> params) {
        Log.i("请求网络前封装参数", "          ");
        params.put("app_name", "joke_essay");
        params.put("version_name", "5.7.0");
        params.put("ac", "wifi");
        params.put("device_id", "30036118478");
        params.put("device_brand", "Xiaomi");
        params.put("update_version_code", "5701");
        params.put("manifest_version_code", "570");
        params.put("longitude", "113.000366");
        params.put("latitude", "28.171377");
        params.put("device_platform", "android");
        onPreExecute();
    }

    public void onPreExecute() {
    }

    ;

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> clazz = HttpUtils.analysisClazzInfo(this);
        LogUtils.e("clazzName --> " + clazz.getName());
        //从文件中读取数据json字串
        String infoFile = Environment.getExternalStorageDirectory().toString() +
                File.separator + "info.txt";
        FileReader fis = null;
        try {
            fis = new FileReader(infoFile);
            BufferedReader bufferedReader = new BufferedReader(fis);
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
            }

            result = sb.toString();
            //LogUtils.e(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        T objResponse = (T) gson.fromJson(result, HttpUtils.analysisClazzInfo(this));
        Log.i("TAG==================", result);

        onSuccess(objResponse);
    }

    public abstract void onSuccess(T result);
}
