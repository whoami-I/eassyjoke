package com.baimeng.eassyjoke.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntDef;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {
    private final int jobWakeUp = 1 ;
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobWakeUp, new ComponentName(this, JobWakeUpService.class));
        jobBuilder.setPeriodic(2000);
        JobInfo jobInfo = jobBuilder.build();
        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮询，看我们的service有没有被杀死
        boolean messageServiceAlive = serviceAlive(MessageService.class.getName());
        if(!messageServiceAlive){
            startService(new Intent(this,MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private boolean serviceAlive(String serviceName){
        boolean isWork = false ;
        ActivityManager myAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myLis = myAM.getRunningServices(100);
        if(myLis.size() <=0 ){
            return false ;
        }
        for (int i = 0 ; i <myLis.size() ; i++){
            String name = myLis.get(i).service.getClassName().toString();
            if(name.equals(serviceName)){
                isWork = true ;
                break;
            }
        }
        return isWork ;
    }
}
