package com.baimeng.framelibrary.skin.config;

import android.content.Context;

/**
 * Created by BaiMeng on 2017/7/31.
 */
public class SkinPreUtils {

    private static SkinPreUtils mInstance ;
    private Context mContext;

    private SkinPreUtils (Context context){
        this.mContext = context.getApplicationContext() ;
    }

    public static SkinPreUtils getInstance(Context context){
        if(mInstance == null){
            synchronized (SkinPreUtils.class){
                if(mInstance == null){
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance ;
    }

    /**
     * 保存皮肤路径
     * @param skinpath
     */
    public void savaSkinPath(String skinpath){
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE).edit()
                .putString(SkinConfig.SKIN_PATH,skinpath).commit();
    }

    /**
     * 获取皮肤路径
     * @return
     */
    public String getSkinPath(){
       return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH,"");
    }

    public void clearSkinPath (){
        savaSkinPath("");
    }
}
