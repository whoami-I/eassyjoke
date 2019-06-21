package com.baimeng.library.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baimeng.library.ioc.ViewUtils;

/**
 * Created by Administrator on 2017/7/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ViewUtils.inject(this);
        initTitle();
        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void setContentView();

    protected <T extends View> T viewById(int id) {
        return (T) findViewById(id);
    }

    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
