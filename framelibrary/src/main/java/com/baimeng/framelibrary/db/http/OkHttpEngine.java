package com.baimeng.framelibrary.db.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import com.baimeng.library.http.EngineCallBack;
import com.baimeng.library.http.HttpUtils;
import com.baimeng.library.http.IHttpEngine;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/3/4.
 * Version 1.0
 * Description: OkHttp默认的引擎
 */
public class OkHttpEngine implements IHttpEngine {

    private static Handler mHandler = new Handler();

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void post( Context context,boolean isCache , String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String jointUrl = HttpUtils.joinParams(url, params);  //打印
        Log.e("Post请求路径：", jointUrl);

        // 了解 Okhhtp
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 这个 两个回掉方法都不是在主线程中
                        final String result = response.body().string();
                        Log.e("Post返回结果：", jointUrl);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void cancelRequest(Object tag) {

    }

    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    public void get( Context context,final boolean isCache , String url, Map<String, Object> params, final EngineCallBack callBack) {
        // 请求路径  参数 + 路径代表唯一标识
        final String finalUrl = HttpUtils.joinParams(url, params);
        Log.e("Get请求路径：", finalUrl);

        // 1.判断需不需要缓存，然后判断有没有
        if (isCache) {
            String resultJson = CacheDataUtil.getCacheResultJson(finalUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                Log.e("TAG", "以读到缓存");
                // 需要缓存，而且数据库有缓存,直接就去执行，里面执行成功
                callBack.onSuccess(resultJson);
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(finalUrl).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i("请求借口失败==========",e.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();
                Log.i("请求借口成功==========",resultJson);
                // 获取数据之后会执行成功方法
                if (isCache) {
                    String cacheResultJson = CacheDataUtil.getCacheResultJson(finalUrl);
                    if (!TextUtils.isEmpty(resultJson)) {
                        // 比对内容
                        if (resultJson.equals(cacheResultJson)) {
                            // 内容一样，不需要执行成功成功方法刷新界面
                            Log.e("数据和缓存一致：", resultJson);
                            return;
                        }
                    }
                }
                // 2.2 执行成功方法
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(resultJson);
                    }
                });
                Log.e("Get返回结果：", resultJson);
                if (isCache) {
                    // 2.3 缓存数据
                    CacheDataUtil.cacheData(finalUrl, resultJson);
                }
            }
        });
    }
}
