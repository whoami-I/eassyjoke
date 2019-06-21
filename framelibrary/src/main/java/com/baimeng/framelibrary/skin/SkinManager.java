package com.baimeng.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.skin.attr.SkinView;
import com.baimeng.framelibrary.skin.callback.ISkinChangeListener;
import com.baimeng.framelibrary.skin.config.SkinConfig;
import com.baimeng.framelibrary.skin.config.SkinPreUtils;
import com.baimeng.library.utils.LogUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/29.
 * 皮肤管理类
 */


public class SkinManager {

    private static SkinManager mInstance ;
    private Context mContext ;
    private Map<ISkinChangeListener,List<SkinView>> mSkinViews = new HashMap<>() ;
    private SkinResources mSkinResource ;
    static {
        mInstance = new SkinManager() ;
    }

    public static SkinManager getInstance() {
        return mInstance ;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
        // 每一次打开应用都会到这里来，防止皮肤被任意删除，做一些措施
        String skinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        Log.e("SkinManager","=================init");
        Log.e("上次路径==================",skinPath+"=============路径");
        File file = new File(skinPath);
        if(!file.exists()){
            // 皮肤apk不存在，清空皮肤
            SkinPreUtils.getInstance(mContext).clearSkinPath();
            return;
        }
        // 最好做一下  能不能获取到包名，检查是否是个正确的皮肤apk
        String packageName = mContext.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        Log.i("上次包名==================",packageName);
        if(TextUtils.isEmpty(packageName)){
            SkinPreUtils.getInstance(mContext).clearSkinPath();
            return;
        }

        // 最好校验签名  增量更新再说


        // 做一些初始化的工作,
        mSkinResource = new SkinResources(mContext,skinPath);
    }

    public int loadSkin(String skinPath) {
        //加载皮肤时，校验皮肤apk
        File file = new File(skinPath);
        if(!file.exists()){
            //文件不存在
            return SkinConfig.SKIN_FILE_NOEXSIST ;
        }
        //校验一下看能否获取到包名
        String packageName = mContext.getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        if(TextUtils.isEmpty(packageName)){
            return SkinConfig.SKIN_FILE_ERROR ;
        }

        //取出上次保存的皮肤路径
        String preSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        //如果本次皮肤和上次皮肤一样，就不要换了
        if(skinPath.equals(preSkinPath)){
            return SkinConfig.SKIN_CHANGE_NOTHING ;
        }



        //校验签名 （增量更新讲）

        //初始化资源管理
        mSkinResource = new SkinResources(mContext ,skinPath);

        //换肤
        changeSkin();

        //保存新的皮肤路径
        saveSkinStatus(skinPath);

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    private void saveSkinStatus(String skinPath) {
        SkinPreUtils.getInstance(mContext).savaSkinPath(skinPath);
    }

    private void changeSkin() {
        //改变皮肤
        //遍历集合（包括说有需要换肤的view）
        Set<ISkinChangeListener> keys = mSkinViews.keySet();
        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews = mSkinViews.get(key);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            key.changeSkin(mSkinResource);
        }
    }

    public int restoreDefault() {
        String skinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(skinPath)){
            return SkinConfig.SKIN_CHANGE_NOTHING ;
        }
        //当前运行的apk的路径
        String localSkinPath = mContext.getPackageResourcePath();
        mSkinResource = new SkinResources(mContext,localSkinPath);
        changeSkin();
        //清空皮肤路径
        SkinPreUtils.getInstance(mContext).clearSkinPath();
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    /**
     * 获取skinviews通过activity
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public void register(ISkinChangeListener listener, List<SkinView> skinViews) {
        mSkinViews.put(listener,skinViews);
    }

    public SkinResources getSkinResource(){
        return mSkinResource ;
    }

    public void checkChangeSkin(SkinView skinView) {
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if(!TextUtils.isEmpty(currentSkinPath)){
            skinView.skin();
        }
    }

    public void unRegister(ISkinChangeListener listener) {
        mSkinViews.remove(listener);
    }
}
