package com.cubebox.tiaoyitiao.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cubebox.tiaoyitiao.AssistentView;
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


/**
 * Created by luozi on 2017/3/17.
 */

public class AssistentService extends BaseService {
    public static final String TAG = "AssistentService";

    private final static int GRAY_SERVICE_ID = -1001;


//    private localBinder mBinder;
//    private MyServiceConnection mMyServiceConnection;


    //要引用的布局文件.
    LinearLayout toucherLayout;
    LinearLayout toucherLayout2;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;
    private CycleUtile cycleUtile;

    int selectBtn = 1;//1红色按钮 2.蓝色按钮
    float rate = 1f;
    int MAX_TIME = 3;//自动跳倒计时单位秒
    int time = MAX_TIME;

    @Override
    public void initObject() {
        super.initObject();
    }

    @Override
    public void initData() {
        super.initData();

        Log.e(TAG, TAG + " onCreate() executed");
//        mBinder = new localBinder();
//        if (mMyServiceConnection == null) {
//            mMyServiceConnection = new MyServiceConnection();
//        }

        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        cycleUtile = new CycleUtile();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.e(TAG, TAG + " onStartCommand() executed");

//        startKeepLiveService();
//        startTaskService();
        rate = TxtUtil.getFloat(PreferencesManager.getInstance(this).getString(ConstantsPreference.PRE_RATE, "1"));
        BroadcastUtil.getInstance().registerReceiver(this, BroadcastUtil.OPEN, open);
        BroadcastUtil.getInstance().registerReceiver(this, BroadcastUtil.CLOSE, close);
        //  绑定远程服务
//        bindService(new Intent(this, KeepLiveService.class), mMyServiceConnection, Context.BIND_IMPORTANT);
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

    BroadcastReceiver hide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(3);
        }
    };
    BroadcastReceiver show = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(1);
        }
    };

    /**
     * 1.展示  2. 关闭 3.隐藏
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (windowManager != null && toucherLayout2 != null) {
                        windowManager.removeViewImmediate(toucherLayout2);
                    }
                    toucherLayout2 = null;
                    createToucher();
                    break;
                case 2:
                    if (windowManager != null && toucherLayout != null) {
//                        windowManager.removeView(toucherLayout);
                        windowManager.removeViewImmediate(toucherLayout);
                    }
                    if (windowManager != null && toucherLayout2 != null) {
                        windowManager.removeViewImmediate(toucherLayout2);
                    }
                    toucherLayout = null;
                    toucherLayout2 = null;
                    break;
                case 3:
                    if (windowManager != null && toucherLayout != null) {
//                        windowManager.removeView(toucherLayout);
                        windowManager.removeViewImmediate(toucherLayout);
                    }
                    toucherLayout = null;
                    createHideView();
                    break;
            }
        }
    };


    private void createToucher() {
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
        final RadioButton red = (RadioButton) toucherLayout.findViewById(R.id.red);
        final RadioButton blue = (RadioButton) toucherLayout.findViewById(R.id.blue);
        final Button left = (Button) toucherLayout.findViewById(R.id.left);
        final Button top = (Button) toucherLayout.findViewById(R.id.top);
        final Button bottom = (Button) toucherLayout.findViewById(R.id.bottom);
        final Button right = (Button) toucherLayout.findViewById(R.id.right);
        final Button small = (Button) toucherLayout.findViewById(R.id.small);
        final LinearLayout layout = (LinearLayout) toucherLayout.findViewById(R.id.layout);

        tvRate.setText("敏感度：" + rate);
        red.setChecked(true);

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execShellCmd("input swipe 200 200 400 200 " + (int) (rate * assistentView.getDistance()));
                cycleUtile.startCycle(1000, new CycleUtile.onCycleListener() {
                    @Override
                    public void onCycle() {
                        if (time > 0) {//为避免连续点击
                            time -= 1;
                            jump.setText("自动跳(" + time + ")");
                            jump.setClickable(false);
                        } else {
                            time = MAX_TIME;
                            cycleUtile.stopCycle();
                            jump.setText("自动跳");
                            jump.setClickable(true);
                        }
                    }
                });
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate += 0.01f;
                PreferencesManager.getInstance(getApplicationContext()).putString(PreferencesManager.PRE_RATE, rate + "");
                tvRate.setText("敏感度：" + rate);
            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate -= 0.01f;
                PreferencesManager.getInstance(getApplicationContext()).putString(PreferencesManager.PRE_RATE, rate + "");
                tvRate.setText("敏感度：" + rate);
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBtn = 1;
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBtn = 2;
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistentView.tinyMove(selectBtn, 1, -1f);
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistentView.tinyMove(selectBtn, 2, -1f);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistentView.tinyMove(selectBtn, 1, 1f);
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistentView.tinyMove(selectBtn, 2, 1f);
            }
        });
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(3);
            }
        });

    }

    /**
     * 展示缩小的界面
     */
    private void createHideView() {
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        params.x = 0;
        params.y = ScreenUtil.getScreenHeight(this) - DensityUtil.dip2px(this, 80);

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = DensityUtil.dip2px(this, 100);
        params.height = DensityUtil.dip2px(this, 50);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout2 = (LinearLayout) inflater.inflate(R.layout.view_hide, null);
        //添加toucherlayout
        windowManager.addView(toucherLayout2, params);

        //主动计算出当前View的宽高信息.
        toucherLayout2.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        final Button show = (Button) toucherLayout2.findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeViewImmediate(toucherLayout2);
                createToucher();
            }
        });
    }


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
        handler.sendEmptyMessage(2);
        this.startService(localIntent);
    }

}
