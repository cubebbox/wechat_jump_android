package com.cubebox.tiaoyitiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cubebox.tiaoyitiao.service.AssistentService;
import com.cubebox.tiaoyitiao.utils.BroadcastUtil;
import com.cubebox.tiaoyitiao.utils.notification.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initData();
        initListener();
    }

    private void initData() {
        startService(new Intent(this, AssistentService.class));

        Intent intent = new Intent(MainActivity.this, OpenActivity.class);
        Intent intent2 = new Intent(MainActivity.this, CloseActivity.class);
        NotificationUtils.getInstance(MainActivity.this).showDefaultNotification(null, 2, "跳一跳辅助", "点击关闭辅助", "辅助", -1, 1000, false, true
                , 0, R.mipmap.ic_launcher, intent2, null, true, -1);
        NotificationUtils.getInstance(MainActivity.this).showDefaultNotification(null, 1, "跳一跳辅助", "点击打开辅助", "辅助", -1, 1000, false, true
                , 0, R.mipmap.ic_launcher, intent, null, true, -1);


        Toast.makeText(this, "再通知栏打开与关闭辅助", Toast.LENGTH_LONG).show();
        finish();
    }

    private void initListener() {
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BroadcastUtil.getInstance().sendEmptyBoradcast(MainActivity.this, BroadcastUtil.CLOSE);
            }
        });
    }


}
