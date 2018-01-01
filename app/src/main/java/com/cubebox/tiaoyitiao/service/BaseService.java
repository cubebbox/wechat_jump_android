package com.cubebox.tiaoyitiao.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BaseService extends Service implements serviceInterface {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public final void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {

        initObject();
        initData();
        initListener();
    }

    //=================创建后回调
    @Override
    public void initObject() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    //============================================网络回调

}
