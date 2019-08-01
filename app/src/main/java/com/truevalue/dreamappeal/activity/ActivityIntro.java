package com.truevalue.dreamappeal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.base.BaseActivity;

public class ActivityIntro extends BaseActivity {

    private static final int DELAY = 1000 * 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ActivityIntro.this,ActivityLogin.class);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(0,DELAY);
    }
}
