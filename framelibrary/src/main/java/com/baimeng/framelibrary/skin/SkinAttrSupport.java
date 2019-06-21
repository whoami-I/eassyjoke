package com.baimeng.framelibrary.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.baimeng.framelibrary.skin.attr.SkinAttr;
import com.baimeng.framelibrary.skin.attr.SkinType;
import com.baimeng.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 * 皮肤属性解析的支持类
 */

public class SkinAttrSupport {

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int attrLength = attrs.getAttributeCount();
        //遍历view的属性，将可以换肤的属性存入集合（color,background,textcolor等）
        for (int i = 0 ; i < attrLength ; i++){
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            //Log.e("TAG","attrName --->>>"+attrName+"attrValue ---->>>"+attrValue);
            //只关注重要的
            //获取的可以换肤的属性
            SkinType skinType = getSkinType(attrName);
            if(skinType != null){
                //资源名称 目前只有attrValue 是一个 @int类型
                String resName = getResName(context,attrValue);
                //如果资源名称为null
                if (TextUtils.isEmpty(resName)){
                    continue;
                }
                //如果不为空，将资源名称和view属性类型封装成SkinAttr，并存入集合
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    /**
     * 获取资源名称
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        //如果是以“@”开头表示引用了资源文件,否则是颜色“#00ffffff”
        if(attrValue.startsWith("@")){
            //去掉“@”
            attrValue = attrValue.substring(1);
            int resId = Integer.parseInt(attrValue);
            //通过id找到资源名称
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     * 通过名称获取SkinType
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(attrName)){
                return skinType ;
            }
        }
        return null;
    }
}
