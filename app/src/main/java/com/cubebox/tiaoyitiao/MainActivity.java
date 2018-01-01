package com.cubebox.tiaoyitiao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
    }

    private void initListener() {
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpenActivity.class);
                NotificationUtils.getInstance(MainActivity.this).showIntentNotification(1, "跳一跳辅助", "点击打开辅助", "辅助", R.mipmap.ic_launcher, intent, null
                        , true, -1);
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
