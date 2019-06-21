package com.baimeng.framelibrary.db.http;

/**
 * Created by Administrator on 2017/7/23.
 * 缓存的实体类
 */

public class CacheData {
    //请求的连接
    private String mUrlKey ;
    //后台返回的json
    private String mResultJson ;

    public CacheData() {
    }

    public CacheData(String mUrlKey, String mResultJson) {
        this.mUrlKey = mUrlKey;
        this.mResultJson = mResultJson;
    }

    public String getUrlKey() {
        return mUrlKey;
    }

    public void setUrlKey(String mUrlKey) {
        this.mUrlKey = mUrlKey;
    }

    public String getResultJson() {
        return mResultJson;
    }

    public void setResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }
}
