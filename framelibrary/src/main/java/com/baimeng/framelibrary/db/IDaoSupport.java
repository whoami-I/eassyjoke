package com.baimeng.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public interface IDaoSupport<T> {
    void init(SQLiteDatabase database, Class<T> clazz);

    long insert(T t);

    //批量插入
    void insert(List<T> datas);

    //获取专门的查询类
    QuerySupport<T> querySupport();


    //List<T> query() ;

    int delete(String whereClause , String... whereArgs);

    int update(T obj , String whereClause , String... whereArgs);
}
