package com.baimeng.library.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/15.
 */

public interface EngineCallBack {
    public void onError(Exception e);

    public void onSuccess(String result);

    public void onPreExecute(Context context, Map<String, Object> params);

    /**
     * 默认的回调，如果没有设置回调就会用这个
     */
    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }
    };

}
