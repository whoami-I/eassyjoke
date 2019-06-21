package com.baimeng.framelibrary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 * 数据库查询支持
 */

public class QuerySupport<T> {
    private SQLiteDatabase mSqliteDataBase ;
    private Class<T> mClazz ;
    //查询条件
    private String mSelection ;
    //查询条件参数
    private String[] mSelectionArgs;
    //查询的列
    private String[] mColumns;
    //分组查询
    private String mQueryGroupBy;
    // 查询对结果集进行过滤
    private String mQueryHaving;
    // 查询排序
    private String mQueryOrderBy;
    // 查询可用于分页
    private String mQueryLimit;

    public QuerySupport(SQLiteDatabase mSqliteDataBase, Class<T> mClazz) {
        this.mSqliteDataBase = mSqliteDataBase ;
        this.mClazz = mClazz ;
    }

    //查询的条件 如name,age等
    public QuerySupport selection(String whereClause){
       this.mSelection = whereClause + " = ?" ;
        return this;
    }

    //查询条件的值
    public QuerySupport selectionArgs(String... selectionArgs){
        this.mSelectionArgs = selectionArgs ;
        return this;
    }

    //查询的列
    public QuerySupport columns(String... columns){
        this.mColumns = columns ;
        return this;
    }

    //分组查询
    public QuerySupport groupBy(String queryGroupBy){
        this.mQueryGroupBy = queryGroupBy;
        return this;
    }

    // 查询对结果集进行过滤
    public QuerySupport queryHaving(String queryHaving){
        this.mQueryHaving = queryHaving ;
        return this;
    }
    // 查询排序
    public QuerySupport queryOrderBy(String queryOrderBy){
        this.mQueryOrderBy = queryOrderBy ;
        return this;
    }
    // 查询可用于分页
    public QuerySupport queryLimit(String queryLimit){
        this.mQueryLimit = queryLimit ;
        return this;
    }

    public List<T> query(){
        Cursor cursor = mSqliteDataBase.query(DaoUtils.getTableName(mClazz), mColumns, mSelection,
                mSelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
        clearQueryParams();
        return cursorToList(cursor);
    }

    public List<T> queryAll(){
        Cursor cursor = mSqliteDataBase.query(DaoUtils.getTableName(mClazz), null, null,
                null, null, null, null, null);
        clearQueryParams();
        return cursorToList(cursor);
    }

    /**
     * 清空参数，没查询一次要清空一下
     */
    private void clearQueryParams() {
        mSelection = null ;
        mSelectionArgs = null ;
        mColumns = null ;
    }

    private List<T> cursorToList(Cursor cursor) {
        List<T> list = null ;
        if(cursor != null && cursor.moveToFirst()){
            list = new ArrayList<>();
            do {
                try {
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String name = field.getName();
                        int index = cursor.getColumnIndex(name);
                        if(index == -1){
                            continue;
                        }
                        //通过反射获取游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        if(cursorMethod != null){
                            Object value = cursorMethod.invoke(cursor,index);
                            if(value == null){
                                continue;
                            }
                            //处理一些特殊的部分
                            if(field.getType() == boolean.class || field.getType() == Boolean.class){
                                if("0".equals(String.valueOf(value))){
                                    value = false ;
                                }else if("1".equals(String.valueOf(value))){
                                    value = true ;
                                }
                            }else if(field.getType() == char.class || field.getType() == Character.class){
                                value = ((String)value).charAt(0);
                            }else if(field.getType() == Date.class){
                                long date = (long) value ;
                                if(date < 0){
                                    value = null ;
                                }else {
                                    value = new Date(date) ;
                                }
                            }
                            field.set(instance,value);
                        }
                    }
                    list.add(instance);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private Method cursorMethod(Class<?> type) throws NoSuchMethodException {
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName,int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> type) {
        String typeName ;
        if(type.isPrimitive()){
            typeName = DaoUtils.capitalize(type.getName());
        }else {
            typeName = type.getSimpleName() ;
        }
        String methodName = "get" + typeName ;
        if("getBoolean".equals(methodName)){
            methodName = "getInt";
        }else if("getChar".equals(methodName)||"getCharacter".equals(methodName)){
            methodName = "getString";
        }else if("getDate".equals(methodName)){
            methodName = "getLong" ;
        }else if("getInteger".equals(methodName)){
            methodName = "getInt" ;
        }
        return methodName;
    }

}
