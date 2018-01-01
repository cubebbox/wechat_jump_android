package com.cubebox.tiaoyitiao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cubebox.tiaoyitiao.utils.BroadcastUtil;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);


        initData();
        initListener();
    }

    private void initData() {
        BroadcastUtil.getInstance().sendEmptyBoradcast(this, BroadcastUtil.OPEN);
        finish();
    }

    private void initListener() {
    }


}
