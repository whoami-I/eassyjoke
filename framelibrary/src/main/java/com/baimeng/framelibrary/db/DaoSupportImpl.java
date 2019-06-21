package com.baimeng.framelibrary.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DaoSupportImpl<T> implements IDaoSupport<T> {
    private SQLiteDatabase mSqliteDataBase;
    private Class<T> mClazz;
    private static final Object[] mPutMethodArgs = new Object[2] ;
    private static final Map<String,Method> mPutMethod = new ArrayMap<>();

    @Override
    public long insert(T obj) {
        ContentValues values = getContentValueByObj(obj);
        return mSqliteDataBase.insert(DaoUtils.getTableName(mClazz),null,values);
    }

    @Override
    public void insert(List<T> datas) {
        mSqliteDataBase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSqliteDataBase.setTransactionSuccessful();
        mSqliteDataBase.endTransaction();
    }

    private QuerySupport<T> mQuerySupport ;
    @Override
    public QuerySupport<T> querySupport() {
        if(mQuerySupport == null){
            mQuerySupport = new QuerySupport<T>(mSqliteDataBase,mClazz);
        }
        return mQuerySupport;
    }

//    /**
//     * 查询全部
//     * @return
//     */
//    @Override
//    public List<T> query() {
//        Cursor cursor = mSqliteDataBase.query(DaoUtils.getTableName(mClazz), null, null, null, null, null, null);
//        return cursorToList(cursor);
//    }

    /**
     *
     * @param whereClause 条件如name ，age 等
     * @param whereArgs  条件的值 如 张三 ，24 等
     * @return
     */
    @Override
    public int delete(String whereClause, String[] whereArgs) {
        return mSqliteDataBase.delete(DaoUtils.getTableName(mClazz),whereClause +" = ?",whereArgs);
    }

    /**
     *
     * @param obj  对象 如Person
     * @param whereClause  条件
     * @param whereArgs  条件的值
     * @return
     */
    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = getContentValueByObj(obj);
        return mSqliteDataBase.update(DaoUtils.getTableName(mClazz),values,whereClause + " = ?",whereArgs);
    }

    //利用反射将泛型类转换成ContentValue
    private ContentValues getContentValueByObj(T obj) {
        ContentValues values = new ContentValues() ;
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(obj);
                mPutMethodArgs[0] = key ;
                mPutMethodArgs[1] = value ;
                String filedTypeName= field.getType().getName();
                Method putMethod = mPutMethod.get(field.getType().getName());
                if(putMethod == null){
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethod.put(filedTypeName,putMethod);
                }
                //value应该是类型Integer，Long等等

                putMethod.invoke(values,key,value);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null ;
                mPutMethodArgs[1] = null ;
            }
        }
        return values ;
    }

    public void init (SQLiteDatabase database ,Class<T> clazz){
        this.mSqliteDataBase = database ;
        this.mClazz = clazz ;
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ").append(DaoUtils.getTableName(clazz)).append("(id integer primay key auto_increment, ");
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);
            String name = field.getName();
            String type = field.getType().getSimpleName();
            sb.append(name).append(DaoUtils.getColumnType(type)).append(", ");
        }
        StringBuffer sqliteStr = sb.replace(sb.length() - 2, sb.length(), ");");
        mSqliteDataBase.execSQL(sqliteStr.toString());
    }


}
