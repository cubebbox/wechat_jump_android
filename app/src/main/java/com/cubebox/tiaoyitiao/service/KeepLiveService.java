package com.cubebox.tiaoyitiao.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.cubebox.tiaoyitiao.utils.CycleUtile;
import com.cubebox.tiaoyitiao.MyProcessAIDL;

import java.util.List;



/**
 * 5.0 以下的包活服务,使用AIDL 双守护进程
 * Created by Administrator on 2017/3/9.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KeepLiveService extends Service implements CycleUtile.onCycleListener {
    public static final String TAG = "KeepLiveService";
    private CycleUtile cycleUtile = null;
    private final static int GRAY_SERVICE_ID = -1001;

    final int CYCLE_TIME = 60 * 1000;
    private localBinder mBinder;
    private MyServiceConnection mMyServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "KeepLiveService onCreate() executed");
        mBinder = new localBinder();
        if (mMyServiceConnection == null) {
            mMyServiceConnection = new MyServiceConnection();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.e(TAG, "KeepLiveService onStartCommand() executed");

        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.AssistentService");
        if (!b) {
            Intent service = new Intent(getApplicationContext(), AssistentService.class);
            startService(service);
        }

//        if (cycleUtile == null) {
//            cycleUtile = new CycleUtile();
//            MLog.stressD("KeepLiveService 重新实例化循环");
//        }
//
//        if (!cycleUtile.isCycle()) {
//            cycleUtile.startCycle(CYCLE_TIME, this);
//            MLog.stressD("KeepLiveService 重新开启循环 ");
//        }

        //  绑定远程服务
        bindService(new Intent(this, KeepLiveService.class), mMyServiceConnection, Context.BIND_IMPORTANT);

        return START_STICKY;//当在内存不足时被回收后，有内存时又会被重新启动
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //通过Aidl进行通讯
    private class localBinder extends MyProcessAIDL.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return TAG;
        }
    }

    //连接远程服务
    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与远程服务通信
                MyProcessAIDL process = MyProcessAIDL.Stub.asInterface(service);
                Log.e(TAG, "连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.e(TAG, "远程服务挂掉了,远程服务被杀死");
            // 启动远程服务
            startService(new Intent(KeepLiveService.this, AssistentService.class));
            // 绑定远程服务
            bindService(new Intent(KeepLiveService.this, AssistentService.class), mMyServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onCycle() {
        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.AssistentService");
        if (!b) {
            Intent service = new Intent(getApplicationContext(), AssistentService.class);
            startService(service);
        }

    }

    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(getApplicationContext(), KeepLiveService.class); //销毁时重新启动Service
        this.startService(localIntent);
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
