package com.baimeng.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by BaiMeng on 2017/3/8.
 */
public class Base64Utils {
    // 加密
    public static String getBase64(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 解密
    public static String getFromBase64(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String bitmapEncode(String path) {
        //decode to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
        //convert to byte array
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public static void bitmapDecode(String encode) {
        byte[] decode = Base64.decode(encode, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        //save to image on sdcard
        saveBitmap(bitmap);
    }

    private static void saveBitmap(Bitmap bitmap) {
        try {
            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/decodeImage.jpg";
//            Log.d("linc","path is "+path);
            OutputStream stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.close();
            //         Log.e("linc","jpg okay!");
        } catch (IOException e) {
            e.printStackTrace();
            //   Log.e("linc","failed: "+e.getMessage());
        }
    }
}
