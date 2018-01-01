package com.cubebox.tiaoyitiao.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

/**
 * 5.0 以上的任务服务
 * Created by Administrator on 2017/3/9.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TaskService extends JobService {
    private final String TAG = "TaskService";
    private final static int JOB_INTERVAL = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, AssistentService.class));
        startJobScheduler();
    }

    public void startJobScheduler() {
        try {
            JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(getPackageName(), TaskService.class.getName()));
            builder.setPeriodic(JOB_INTERVAL);//设置间隔时间
            builder.setPersisted(true);//设备重启之后你的任务是否还要继续执行
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE); //设置需要的网络条件，默认NETWORK_TYPE_NONE
//            builder.setMinimumLatency(3000);// 设置任务运行最少延迟时间
            builder.setOverrideDeadline(50000);// 设置deadline，若到期还没有达到规定的条件则会开始执行
//            builder.setRequiresCharging(true);// 设置是否充电的条件,默认false
//            builder.setRequiresDeviceIdle(false);// 设置手机是否空闲的条件,默认false

            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        startMainService();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        startMainService();
        return false;
    }

    private void startMainService() {
        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.AssistentService");
        if (!b) {
            Intent service = new Intent(getApplicationContext(), AssistentService.class);
            startService(service);
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param context
     * @param className 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
