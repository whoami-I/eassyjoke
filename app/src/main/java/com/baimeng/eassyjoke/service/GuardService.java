package com.baimeng.eassyjoke.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baimeng.eassyjoke.ProcessConnection;

/**
 * Created by Administrator on 2017/8/3.
 */

public class GuardService extends Service {

    private final int GuardId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        //绑定服务
        bindService(new Intent(this,MessageService.class),mServiceConnection,BIND_IMPORTANT);
        startForeground(GuardId,new Notification());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){};
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            Log.e("GuardService","建立连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            startService(new Intent(GuardService.this,MessageService.class));
            bindService(new Intent(GuardService.this,MessageService.class),mServiceConnection,BIND_IMPORTANT);
        }
    };

}
