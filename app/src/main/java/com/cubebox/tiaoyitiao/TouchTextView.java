package com.cubebox.tiaoyitiao;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by luozi on 2017/12/30.
 */

public class TouchTextView extends AppCompatTextView {
    public TouchTextView(Context context) {
        this(context, null);
    }

    public TouchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

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
    /**
     * 执行shell命令
     *
     */
    private void execShellCmd( ) {

        //每10s产生一次点击事件，点击的点坐标为(0.2W - 0.8W,0.2H - 0.8 H),W/H为手机分辨率的宽高.
        new Thread(new Runnable() {
            @Override
            public void run() {

                //生成点击坐标
                int x = (int) (Math.random() * 1080 * 0.6 + 1080 * 0.2);
                int y = (int) (Math.random() * 1920 * 0.6 + 1920 * 0.2);
                //利用ProcessBuilder执行shell命令
                String[] order = {
                        "input",
                        "tap",
                        "" + x,
                        "" + y
                };
                Log.e("test", "luozisong");
                try {
                    new ProcessBuilder(order).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
