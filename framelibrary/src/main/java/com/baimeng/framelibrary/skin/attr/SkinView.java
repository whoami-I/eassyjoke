package com.baimeng.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */

public class SkinView {
    private View mView ;
    private List<SkinAttr> mAttrs ;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view ;
        this.mAttrs = skinAttrs ;
    }

    public void skin(){
        for (SkinAttr attr : mAttrs) {
            attr.skin(mView );
        }
    }

    @Override
    public String toString() {
        return "SkinView{" +
                "mView=" + mView +
                ", mAttrs=" + mAttrs +
                '}';
    }
}
