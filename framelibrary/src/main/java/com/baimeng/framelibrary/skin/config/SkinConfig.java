package com.baimeng.framelibrary.skin.config;

/**
 * Created by BaiMeng on 2017/7/31.
 */
public class SkinConfig {
    //SP文件名称
    public static final String SKIN_INFO_NAME = "skin_info";
    //保存皮肤路径到sp的key
    public static final String SKIN_PATH = "skinpath" ;
    // 不需要改变任何东西
    public static final int SKIN_CHANGE_NOTHING = -1;
    // 换肤成功
    public static final int SKIN_CHANGE_SUCCESS = 1;
    //皮肤文件不存在
    public static final int SKIN_FILE_NOEXSIST = -2;
    // 皮肤文件有错误可能不是一个apk文件
    public static final int SKIN_FILE_ERROR = -3;
}
