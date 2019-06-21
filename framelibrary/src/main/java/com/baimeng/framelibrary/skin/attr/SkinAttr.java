package com.baimeng.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by Administrator on 2017/7/29.
 */

public class SkinAttr {
    private String mResName ;
    private SkinType mSkinType ;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName ;
        this.mSkinType = skinType;
    }

    public void skin(View mView) {
        mSkinType.skin(mView,mResName);
    }

    @Override
    public String toString() {
        return "SkinAttr{" +
                "mResName='" + mResName + '\'' +
                ", mSkinType=" + mSkinType +
                '}';
    }
}
