package com.baimeng.library.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/15.
 */

public interface IHttpEngine {

    //1.get请求
    void get(Context context, boolean isCache, String url, Map<String, Object> params, EngineCallBack callBack);

    //2.post请求
    void post(Context context, boolean isCache, String url, Map<String, Object> params, EngineCallBack callBack);

    void cancelRequest(Object tag);

    //3.下载文件


    //4.上传文件


    //5.https 添加证书


}
