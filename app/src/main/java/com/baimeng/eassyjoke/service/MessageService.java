package com.baimeng.eassyjoke.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baimeng.eassyjoke.ProcessConnection;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MessageService extends Service {

    private final int MessageId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Log.e("TAG","等待接收消息");
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this,GuardService.class),mServiceConnection,BIND_IMPORTANT);
        //提高服务优先级
        startForeground(MessageId,new Notification());
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
            Log.e("MessageService","建立连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            startService(new Intent(MessageService.this,GuardService.class));
            bindService(new Intent(MessageService.this,GuardService.class),mServiceConnection,BIND_IMPORTANT);
        }
    };
}
