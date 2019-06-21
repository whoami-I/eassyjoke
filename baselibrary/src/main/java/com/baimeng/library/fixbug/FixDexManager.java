package com.baimeng.library.fixbug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Administrator on 2017/7/9.
 */

public class FixDexManager {
    private Context mContext;

    private File mDexFix;

    public FixDexManager(Context context) {
        this.mContext = context;
        //获取应用可以访问的dex目录
        this.mDexFix = context.getDir("odex", Context.MODE_PRIVATE);
    }

    /**
     * 修复dex包
     *
     * @param path
     */
    public void fixDex(String path) throws Exception {
        //1.先获取已经运行的DexElement
        ClassLoader classLoader = mContext.getClassLoader();
        Object applictionDexElement = getDexElementByClassLoader(classLoader);

        //2.获取下载好的补丁的dexElement

        //2.1移动到系统能够访问的 dex目录下  ClassLoader
        File srcFile = new File(path);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(path);
        }

        File destFile = new File(mDexFix, srcFile.getName());
        if (destFile.exists()) {
            Log.i("srcFile已经存在", "++++++++++++++++++++++++++=" + srcFile.getName());
            return;
        }

        copyFile(srcFile, destFile);

        //2.2 ClassLoader读取fixDex路径
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(destFile);

        File optimizedDirectory = new File(mDexFix, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdirs();
        }

        //修复

        for (File fixDexFile : fixDexFiles) {
            //dexPath dex路径    必须要在应用目录下的dex文件中
            //optimizedDirectory 解压路径
            //libraryPath .so文件位置
            //parent 父ClassLoader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),
                    optimizedDirectory,
                    null,
                    classLoader);
            Object fixDexElements = getDexElementByClassLoader(fixDexClassLoader);
            //3.把补丁的dexElement插到运行的dexElement前面
            //合并classLoader数组和fixDexElement数组
            Log.e("走到这了吗？", "合并之前");
            applictionDexElement = combineArray(fixDexElements, applictionDexElement);
        }

        //把合并的数组注入到原来的applicationClassLoader中
        injectDexElement(classLoader, applictionDexElement);

    }

    private void injectDexElement(ClassLoader classLoader, Object dexElement) throws Exception {
        //先获取pathList

        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);


        //2.获取pathList的dexElement

        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);
        dexElementField.set(pathList, dexElement);
    }


    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {

        //先获取pathList

        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);


        //2.获取pathList的dexElement

        Field dexElementField = pathList.getClass().getDeclaredField("dexElements");
        dexElementField.setAccessible(true);
        return dexElementField.get(pathList);
    }

    //拷贝文件
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
        Log.e("走这了吗？", "++++++++++++++++++拷贝文件");
    }
    //合并数组

    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
                Log.e("打印元素", Array.get(arrayLhs, k).toString());
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - 1));
                Log.e("打印元素", Array.get(arrayRhs, k - 1).toString());
            }
        }
        return result;
    }

//    public void loadFixDex() {
//        //加载全部的dex包
//        File[] dexFiles = mDexFix.listFiles();
//        List<File> fixDexFiles = new ArrayList<>();
//        for (File dexFile : dexFiles) {
//            fixDexFiles.add(dexFile);
//        }
//        fixDexFiles(fixDexFiles);
//    }
}
