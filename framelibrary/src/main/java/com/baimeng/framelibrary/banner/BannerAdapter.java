package com.baimeng.framelibrary.banner;

import android.view.View;

/**
 * Created by Administrator on 2017/8/9.
 */

public abstract class BannerAdapter {
    public abstract View getView(int positon , View convertview) ;

    public abstract int getCount();

    public String getItemDesc(int postion){
        return "" ;
    };
}
