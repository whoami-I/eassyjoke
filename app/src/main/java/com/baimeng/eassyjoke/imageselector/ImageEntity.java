package com.baimeng.eassyjoke.imageselector;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/14.
 */

public class ImageEntity implements Serializable {
    public String path ;
    public String name ;
    public long time ;

    public ImageEntity() {
    }

    public ImageEntity(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ImageEntity){
            ImageEntity compare = (ImageEntity) o ;
            return TextUtils.equals(this.path,compare.path);
        }
        return false ;
    }
}
