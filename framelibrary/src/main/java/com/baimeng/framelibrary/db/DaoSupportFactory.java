package com.baimeng.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.baimeng.library.utils.XPermissionUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/7/19.
 */

public class DaoSupportFactory {
    private static DaoSupportFactory mInstance ;
    private SQLiteDatabase mSqLiteDatabase ;

    //持有外部数据库的引用
    private DaoSupportFactory(){
            File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "nhdz" + File.separator + "database");
            if(!dbRoot.exists()){
                dbRoot.mkdirs();
            }
            File dbFile = new File(dbRoot, "nhdz.db");
            Log.i("=======","数据库创建成功"+dbFile.getAbsolutePath());
            mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

    }
    public static DaoSupportFactory getFactory(){
        if(mInstance == null){
            synchronized (DaoSupportFactory.class){
                if(mInstance == null){
                    mInstance = new DaoSupportFactory() ;
                }
            }
        }
        return mInstance ;
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz){
        IDaoSupport<T> daoSupport = new DaoSupportImpl<>();
        daoSupport.init(mSqLiteDatabase,clazz);
        return daoSupport ;
    }
}
