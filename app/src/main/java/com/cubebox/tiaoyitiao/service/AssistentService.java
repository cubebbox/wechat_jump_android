package com.cubebox.tiaoyitiao.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cubebox.tiaoyitiao.AssistentView;
import com.cubebox.tiaoyitiao.MyProcessAIDL;
import com.cubebox.tiaoyitiao.R;
import com.cubebox.tiaoyitiao.data.ConstantsPreference;
import com.cubebox.tiaoyitiao.utils.BroadcastUtil;
import com.cubebox.tiaoyitiao.utils.CycleUtile;
import com.cubebox.tiaoyitiao.utils.DensityUtil;
import com.cubebox.tiaoyitiao.utils.PreferencesManager;
import com.cubebox.tiaoyitiao.utils.ScreenUtil;
import com.cubebox.tiaoyitiao.utils.TxtUtil;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by luozi on 2017/3/17.
 */

public class AssistentService extends BaseService implements CycleUtile.onCycleListener {
    public static final String TAG = "AssistentService";

    private final static int GRAY_SERVICE_ID = -1001;


    private localBinder mBinder;
    private MyServiceConnection mMyServiceConnection;


    //要引用的布局文件.
    LinearLayout toucherLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    @Override
    public void initObject() {
        super.initObject();
    }

    @Override
    public void initData() {
        super.initData();

        Log.e(TAG, TAG + " onCreate() executed");
        BroadcastUtil.getInstance().registerReceiver(this, BroadcastUtil.OPEN, open);
        BroadcastUtil.getInstance().registerReceiver(this, BroadcastUtil.CLOSE, close);
        mBinder = new localBinder();
        if (mMyServiceConnection == null) {
            mMyServiceConnection = new MyServiceConnection();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.e(TAG, TAG + " onStartCommand() executed");

        startKeepLiveService();
        startTaskService();
        rate = TxtUtil.getFloat(PreferencesManager.getInstance(this).getString(ConstantsPreference.PRE_RATE, "1"));

        //  绑定远程服务
        bindService(new Intent(this, KeepLiveService.class), mMyServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;//当在内存不足时被回收后，有内存时又会被重新启动
    }

    BroadcastReceiver open = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(1);
        }
    };

    BroadcastReceiver close = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(2);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    createToucher();
                    break;
                case 2:
                    if (windowManager != null && toucherLayout != null) {
                        windowManager.removeView(toucherLayout);
                    }
                    break;
            }
        }
    };

    private void createToucher() {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = DensityUtil.dip2px(this, 10);
        params.y = DensityUtil.dip2px(this, 150);

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = ScreenUtil.getScreenWidth(this) - DensityUtil.dip2px(this, 20);
        params.height = ScreenUtil.getScreenHeight(this) - DensityUtil.dip2px(this, 180);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (LinearLayout) inflater.inflate(R.layout.view_assistent, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout, params);

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final AssistentView assistentView = (AssistentView) toucherLayout.findViewById(R.id.view_assistent);
        final Button jump = (Button) toucherLayout.findViewById(R.id.jump);
        final Button add = (Button) toucherLayout.findViewById(R.id.add);
        final Button reduce = (Button) toucherLayout.findViewById(R.id.reduce);
        final TextView tvRate = (TextView) toucherLayout.findViewById(R.id.rate);

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execShellCmd("input swipe 400 600 500 800 " + (int) (rate * assistentView.getDistance()));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate += 0.02f;
                PreferencesManager.getInstance(getApplicationContext()).putString(PreferencesManager.PRE_RATE, rate + "");
                tvRate.setText("敏感度：" + rate);
            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate -= 0.02f;
                PreferencesManager.getInstance(getApplicationContext()).putString(PreferencesManager.PRE_RATE, rate + "");
                tvRate.setText("敏感度：" + rate);
            }
        });
    }

    float rate = 1f;

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
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
            startService(new Intent(AssistentService.this, KeepLiveService.class));
            // 绑定远程服务
            bindService(new Intent(AssistentService.this, KeepLiveService.class), mMyServiceConnection, Context.BIND_IMPORTANT);
        }
    }


    @Override
    public void onCycle() {
        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.KeepLiveService");
        if (!b) {
            Intent service = new Intent(getApplicationContext(), KeepLiveService.class);
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
        if (open != null)
            unregisterReceiver(open);
        if (close != null)
            unregisterReceiver(close);
        Log.e(TAG, TAG + "  onDestroy() executed");
        Intent localIntent = new Intent();
        localIntent.setClass(getApplicationContext(), AssistentService.class); //销毁时重新启动Service
        this.startService(localIntent);
    }

    /**
     * 开启守护服务
     */
    private void startKeepLiveService() {
        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.KeepLiveService");
        if (!b) {
            Intent service = new Intent(getApplicationContext(), KeepLiveService.class);
            startService(service);
        }
    }

    /**
     * 开启任务服务
     */
    private void startTaskService() {
        boolean b = isServiceExisted(getApplicationContext(), "net.chofn.crm.service.TaskService");
        if (!b) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent service = new Intent(getApplicationContext(), TaskService.class);
                startService(service);
            }
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
