package com.baimeng.eassyjoke.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.baimeng.eassyjoke.R;
import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.skin.SkinResources;



/**
 * Created by Administrator on 2017/8/5.
 */

public class WelcomActivity extends Activity {
    private static final long WAIT_TIME =  3000 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //3秒后跳转主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomActivity.this,HomeActivity.class));
                finish();
            }
        },WAIT_TIME);

    }
}
