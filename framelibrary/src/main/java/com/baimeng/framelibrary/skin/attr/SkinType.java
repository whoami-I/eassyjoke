package com.baimeng.framelibrary.skin.attr;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baimeng.framelibrary.skin.SkinManager;
import com.baimeng.framelibrary.skin.SkinResources;

/**
 * Created by Administrator on 2017/7/29.
 */

public enum  SkinType {

    TEXT_COLOR ("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResources skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if (color == null) return;
            TextView textView = (TextView) view ;
            textView.setTextColor(color);
        }
    }, BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            SkinResources skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view ;
                view.setBackgroundDrawable(drawable);
                return;
            }
            ColorStateList color = skinResource.getColorByName(resName);
            if (color!=null){
                view.setBackgroundColor(color.getDefaultColor());
            }


        }
    }, SRC("src") {
        @Override
        public void skin(View view, String resName) {
            SkinResources skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                ImageView imageView = (ImageView) view ;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    public SkinResources getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }

    private String mResName ;

    SkinType(String resName){
        this.mResName = resName ;
    }

    public abstract void skin(View view, String resName) ;

    public String getResName() {
        return mResName ;
    }
}
