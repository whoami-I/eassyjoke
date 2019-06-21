package com.baimeng.library.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2017/4/19.
 */

public class EditorFileUtils {
    public Context mContext;
    /**
     * sd卡根目录
     */
    public static String msdRootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 手机内存的缓存根目录
     */
    public static String mDataRootPath = null;

    /**
     * 保存图片的根目录
     */
    public static String FOLDER_NAME = "/AndroidImage";
    /**
     * 图文详情图片缓存
     */
    public static String IMG_RICHTEXT = "/RichTextImage";

    /**
     * 附件存储目录
     *
     * @param context
     */
    public String attachments = "/attachments";

    public EditorFileUtils(Context context) {
        mContext = context;
        mDataRootPath = context.getCacheDir().getPath();
    }

    /**
     * 获取缓存根目录 如果sd卡挂载就使用sd卡
     *
     * @return
     */
    public static String getRootDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? msdRootPath : mDataRootPath;
    }

    /**
     * 获取图片存储目录
     *
     * @return
     */
    public static String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? msdRootPath + FOLDER_NAME
                : mDataRootPath + FOLDER_NAME;
    }

    /**
     * 获取附件存储目录
     *
     * @return
     */
    public String getAttachmentDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? msdRootPath + attachments
                : mDataRootPath + attachments;
    }

    public static String getRichTextImageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? msdRootPath + IMG_RICHTEXT
                : mDataRootPath + IMG_RICHTEXT;
    }

    public String getFilePathFromUri(Uri uri) {
        //LogUtils.i("scheme--"+uri);
        if (uri == null) {
            //LogUtils.i("uri--null");
            return null;
        }
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            // LogUtils.i("scheme--null");
            data = uri.getPath();
        } else if (mContext.getContentResolver().SCHEME_FILE.equals(scheme)) {
            //LogUtils.i("scheme--file");
            data = uri.getPath();
            //LogUtils.i("data---="+data);
        } else if (mContext.getContentResolver().SCHEME_CONTENT.equals(scheme)) {
            //LogUtils.i("scheme--content");
            Cursor cursor = mContext.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
            }
            //LogUtils.i("data----"+data);
        }
        return data;
    }

}
