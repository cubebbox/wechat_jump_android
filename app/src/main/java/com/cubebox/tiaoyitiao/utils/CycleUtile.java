package com.cubebox.tiaoyitiao.utils;

import android.os.Handler;

public class CycleUtile {
    private long duration = -1;
    private onCycleListener listener = null;
    private boolean isCycle = false;

    public CycleUtile() {

    }

    /**
     * 开始循环
     */
    public CycleUtile startCycle(long duration) {
        this.duration = duration;
        handler.post(runnable);
        isCycle = true;
        return this;
    }

    /**
     * 开始反复，带回调
     */
    public CycleUtile startCycle(long duration, onCycleListener listener) {
        this.duration = duration;
        this.listener = listener;
        handler.post(runnable);
        isCycle = true;
        return this;
    }

    public void stopCycle() {
        handler.removeCallbacks(runnable);
        isCycle = false;
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (listener != null) {
                listener.onCycle();
            }
            handler.postDelayed(runnable, duration);
        }
    };
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }
    };

    public void setListener(onCycleListener listener) {
        this.listener = listener;
    }

    public interface onCycleListener {
        void onCycle();
    }

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
